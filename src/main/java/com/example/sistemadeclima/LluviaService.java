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
                 " | Temp: " + tempActual + "°C" +
                 " | Humedad: " + humidityActual + "%" +
                 " | Viento: " + vientoActual + "km/h (" + vientoDeg + "°)" +
                 " | Presión: " + grnd_levelActual + " hPa" +
                 " | ICON: " + clima.getWeather().get(0).getIcon();

     }
}
