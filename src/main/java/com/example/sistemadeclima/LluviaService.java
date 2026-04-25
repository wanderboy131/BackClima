package com.example.sistemadeclima;

import org.springframework.stereotype.Service;

@Service
public class LluviaService {

     public String estimarLluvia(ClimaResponse clima){

        //Prueba de nueva lógica

         // 1. Datos base
         double tempActual = clima.getMain().getTemp();
         int humidityActual = clima.getMain().getHumidity();
         double grnd_levelActual = clima.getMain().getGrnd_level();
         int cloudsActual = clima.getClouds().getAll();
         double vientoActual = clima.getWind().getSpeed();
         double vientoDeg = clima.getWind().getDeg();

// 2. Lógica de Viento Específica para Valledupar
         boolean esVientoHumedo = (vientoDeg >= 185 && vientoDeg <= 225); // Viene del SSW
         boolean esVientoSeco = (vientoDeg >= 40 && vientoDeg <= 100);    // Viene del NE (Alisios)

         double factorVientoValle;
         if (esVientoHumedo) {
             // Si viene del sur y tiene fuerza, es el motor de la lluvia
             factorVientoValle = (vientoActual > 12.0) ? 1.25 : 1.10;
         } else if (esVientoSeco) {
             // Si viene del NE, es el "freno" de la lluvia (viento seco)
             factorVientoValle = 0.65;
         } else {
             factorVientoValle = 1.0; // Vientos variables o calma
         }

// 3. Punto de Rocío (Fórmula de Magnus)
         double alpha = ((17.625 * tempActual) / (243.04 + tempActual)) + Math.log(humidityActual / 100.0);
         double dewpoint = (243.04 * alpha) / (17.625 - alpha);
         double deltaDew = tempActual - dewpoint;
         double deltaDew_normalizada = Math.exp(-deltaDew / 4.5);

// 4. Normalizaciones y Ajustes
         double humedadNormalizada = (humidityActual - 16.0) / (100.0 - 16.0);
         double nubosidadNormalizada = cloudsActual / 100.0;
         double presionNormalizada = Math.max(0, Math.min(1, (1005.0 - grnd_levelActual) / (1005.0 - 980.0)));
         double vientoNormalizada = Math.min(vientoActual / 25.0, 1.0); // Normalizado a 25km/h max

// Filtro anti-falsos positivos: Si no hay nubes, la humedad no importa tanto
         double nubosidadAjustada = nubosidadNormalizada * (humidityActual > 50 ? 1.1 : 0.7);

// 5. Cálculo del Score con los nuevos pesos (Cruce DHIME/OpenMeteo)
         double score = (deltaDew_normalizada  * 0.35)  // Principal indicador
                 +      (nubosidadAjustada     * 0.30)  // Sin nubes no hay lluvia
                 +      (vientoNormalizada     * 0.25)  // Factor crítico en Valledupar
                 +      (humedadNormalizada    * 0.05)  // Apoyo
                 +      (presionNormalizada    * 0.05); // Poco peso por baja variabilidad

// 6. Aplicación del Factor Heurístico de Dirección
         double probabilidadFinal = (score * 100.0) * factorVientoValle;

// 7. Capado de seguridad (0% - 100%)
         probabilidadFinal = Math.max(0, Math.min(100, probabilidadFinal));

// Si está despejado, castigamos la probabilidad para evitar el error de "Humedad 100%"
         if (cloudsActual < 10) {
             probabilidadFinal *= 0.3;
         }

         double probabilidad = Math.round(probabilidadFinal * 100.0) / 100.0;

         return "Probabilidad de lluvia: " + probabilidad + "%" +
                 " | " + clima.getWeather().get(0).getDescription() +
                 " | Viento: " + vientoActual + "km/h (" + vientoDeg + "°)" +
                 " | ICON: " + clima.getWeather().get(0).getIcon();




       /*
         //Datos main
         double tempActual = clima.getMain().getTemp();
         int humidityActual = clima.getMain().getHumidity();
         double grnd_levelActual = clima.getMain().getGrnd_level();
         //Datos Clouds
         int cloudsActual = clima.getClouds().getAll();

         //Datos Wind
         double vientoActual = clima.getWind().getSpeed();
         double vientoDeg = clima.getWind().getDeg();

         boolean vientoLluvioso = (vientoDeg >= 190 && vientoDeg <= 210);
         double dirFactor = vientoLluvioso ? 1.2 : 0.8;


         //Fórmula Magnus
         double alpha = ((17.625 * tempActual) / (243.04 + tempActual))
                 + Math.log(humidityActual / 100.0);
         double dewpoint = (243.04 * alpha) / (17.625 - alpha);
         double deltaDew = tempActual - dewpoint;


         //Normalización
         double humedadNormalizada = (humidityActual - 16) /(100.0 - 16.0);
         //double tempNormalizada = Math.max(0, Math.min(1, (31.0 - tempActual) / (31.0 - 15.9)));
         double presionNormalizada = Math.max(0, Math.min(1, (1000.2 - grnd_levelActual) / (1000.2 - 980.0)));
         double nubosidadNormalizada = cloudsActual / 100.0;
         double vientoNormalizada = Math.min(vientoActual / 10.0, 1.0 ) * dirFactor;

         //Normalización de punto de rocio progresivo
         double deltaDew_normalizada = Math.exp(-deltaDew / 4.5);

         //Ajuste de nubosidad para que tenga sentido con humedad
         double nubosidadAjustada = nubosidadNormalizada * humedadNormalizada;
         //Penalización de nubosidad para falsos positivos
         if (nubosidadNormalizada < 0.3) {
             nubosidadAjustada *= 0.7;
         }
         //Obtenemos icon y description
         String descripcion = clima.getWeather().get(0).getDescription();
         String icon = clima.getWeather().get(0).getIcon();

         double score = (deltaDew_normalizada  * 0.40)
                 +      (nubosidadAjustada  * 0.25)
                 +      (presionNormalizada *0.20)
                 +      (humedadNormalizada * 0.10)
                 +      (vientoNormalizada  * 0.05);

         double probabilidad = Math.round( score * 10000.0)/100.0;


         return "Probabilidad de lluvia: " + probabilidad + "%" +
                 " | " + descripcion +
                 " | Temp: " + tempActual + "°C" +
                 " | Humedad: " + humidityActual + "%" +
                 " | ICON: " + icon; */

     }
}
