package com.example.sistemadeclima;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class LluviaService {

     public String estimarLluvia(ClimaResponse clima){

         //Datos main
         double tempActual = clima.getMain().getTemp();
         int humidityActual = clima.getMain().getHumidity();
         double grnd_levelActual = clima.getMain().getGrnd_level();
         //Datos Clouds
         int cloudsActual = clima.getClouds().getAll();

         //Datos Wind
         double vientoActual = clima.getWind().getSpeed();
         double vientoDeg = clima.getWind().getDeg();

         boolean vientoLluvioso = (vientoDeg >= 180 && vientoDeg <= 270);
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
         double nubosidadNormalizada = (cloudsActual - 40.0)/(100.0 - 40.0) ;
         double vientoNormalizada = Math.min(vientoActual / 10.0, 1.0 ) * dirFactor;

         //Normalización de punto de rocio con umbrales más reales
         double deltaDew_normalizada;
         if (deltaDew < 4.5) {
             deltaDew_normalizada = 1.0; // saturado, lluvia muy probable
         } else {
             deltaDew_normalizada = Math.max(0, Math.min(1, 1 - (deltaDew / 20.0)));
         }

         //Ajuste de nubosidad para que tenga sentido con humedad
         double nubosidadAjustada = nubosidadNormalizada * humedadNormalizada;

         double score = (deltaDew_normalizada  * 0.40)
                 +      (nubosidadAjustada  * 0.25)
                 +      (presionNormalizada *0.20)
                 +      (humedadNormalizada * 0.10)
                 +      (vientoNormalizada  * 0.05);

         double probabilidad = Math.round( score * 10000.0)/100.0;


         return "Probabilidad de lluvia ahora: " + probabilidad + "%"+
                 " Temperatura actual: " + tempActual + "°C" +
                 " Humedad: " + humidityActual + "%";



     }
}
