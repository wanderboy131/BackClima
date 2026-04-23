package com.example.sistemadeclima;

import java.util.List;

public class ClimaResponse {
       private Main main;
       private Clouds clouds;
       private Wind wind;
       private List<Weather> weather;

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public static class Weather {
        private String description;
        private String icon;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }


    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public static class Main{
          private double temp;
          private int humidity;
          private double grnd_level;

           public double getTemp() {
               return temp;
           }

           public void setTemp(double temp) {
               this.temp = temp;
           }

           public int getHumidity() {
               return humidity;
           }

           public void setHumidity(int humidity) {
               this.humidity = humidity;
           }

           public double getGrnd_level() {
               return grnd_level;
           }

           public void setGrnd_level(double grnd_level) {
               this.grnd_level = grnd_level;
           }
       }

       public static class Clouds {
           private int all;

           public int getAll() {
               return all;
           }

           public void setAll(int all) {
               this.all = all;
           }
       }

       public static class Wind {
           private double speed;
           private int deg;
           private double gust;

           public double getSpeed() {
               return speed;
           }

           public void setSpeed(double speed) {
               this.speed = speed;
           }

           public int getDeg() {
               return deg;
           }

           public void setDeg(int deg) {
               this.deg = deg;
           }

           public double getGust() {
               return gust;
           }

           public void setGust(double gust) {
               this.gust = gust;
           }
       }
}
