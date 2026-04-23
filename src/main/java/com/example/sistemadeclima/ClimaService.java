package com.example.sistemadeclima;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ClimaService {

    private final RestTemplate restTemplate;

    public ClimaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ClimaResponse getClima(double latitud, double longitud){
        String apiKey = "75f5c1a2f8ef9e1d5a7cff5eb90cc447";
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitud + "&lon=" + longitud + "&appid=" + apiKey + "&units=metric&lang=es";

        return restTemplate.getForObject(url, ClimaResponse.class);

    }



}
