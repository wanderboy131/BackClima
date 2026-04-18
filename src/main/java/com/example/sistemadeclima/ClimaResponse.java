package com.example.sistemadeclima;

import com.sun.tools.javac.Main;

public class ClimaResponse {

    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public static class Current{
        private double temperature_2m,  surface_pressure;
        private int relative_humidity_2m, cloud_cover, weather_code, precipitation;

        public double getTemperature_2m() {
            return temperature_2m;
        }

        public void setTemperature_2m(double temperature_2m) {
            this.temperature_2m = temperature_2m;
        }

        public int getPrecipitation() {
            return precipitation;
        }

        public void setPrecipitation(int precipitation) {
            this.precipitation = precipitation;
        }

        public double getSurface_pressure() {
            return surface_pressure;
        }

        public void setSurface_pressure(double surface_pressure) {
            this.surface_pressure = surface_pressure;
        }

        public int getRelative_humidity_2m() {
            return relative_humidity_2m;
        }

        public void setRelative_humidity_2m(int relative_humidity_2m) {
            this.relative_humidity_2m = relative_humidity_2m;
        }

        public int getCloud_cover() {
            return cloud_cover;
        }

        public void setCloud_over(int cloud_cover) {
            this.cloud_cover = cloud_cover;
        }

        public int getWeather_code() {
            return weather_code;
        }

        public void setWeather_code(int weather_code) {
            this.weather_code = weather_code;
        }
    }
}
