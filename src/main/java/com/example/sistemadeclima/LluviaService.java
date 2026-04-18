package com.example.sistemadeclima;

import org.springframework.stereotype.Service;

@Service
public class LluviaService {

        public String estimarLluvia(ClimaResponse clima) {

            double temperatura = clima.getCurrent().getTemperature_2m();
            double presionSuperficial = clima.getCurrent().getSurface_pressure();
            int humedadRelativa = clima.getCurrent().getRelative_humidity_2m();
            int nubosidadTotal = clima.getCurrent().getCloud_cover();
            int codigoTiempo = clima.getCurrent().getWeather_code();

            double puntoRocio = temperatura - ((100 - humedadRelativa) / 5.0);
            double diferenciaTemperatura = temperatura - puntoRocio;
            double deficitPresion = 1013 - presionSuperficial;


            int score = 0;

// base por weathercode
            if (codigoTiempo <= 3)
                score += 0;
            else if (codigoTiempo <= 57)
                score += 30;
            else if (codigoTiempo <= 82)
                score += 70;
            else if (codigoTiempo >= 95)
                score += 90;

// ajustes por factores
            if (humedadRelativa > 90)
                score += 15;
            else if (humedadRelativa > 80)
                score += 8;

            if (diferenciaTemperatura< 3)
                score += 15;
            else if (diferenciaTemperatura < 5)
                score += 8;

            if (deficitPresion > 15)
                score += 10;
            else if (deficitPresion > 8)
                score += 5;

            if (nubosidadTotal > 80)
                score += 10;

            score = Math.min(score, 100);

             return "Probabilidad de lluvia según pesos intuitivos y generales: "
                     + score + " "
                     + "Codigo de tiempo" + " "
                     + codigoTiempo + " "
                     + "humedad: " + " "
                     + humedadRelativa + " "
                     + "presion: " + " "
                     + presionSuperficial + " "
                     + "nubosidad: " + " "
                     + nubosidadTotal + " "
                     + "Temperatura: " + temperatura;

        }

}
