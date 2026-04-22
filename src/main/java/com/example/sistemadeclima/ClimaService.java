package com.example.sistemadeclima;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class ClimaService {

    private final RestTemplate restTemplate;

    public ClimaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ClimaResponse getClima(/*double latitud, double longitud*/){

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=10.4631&lon=-73.2532&appid=75f5c1a2f8ef9e1d5a7cff5eb90cc447&units=metric&lang=es";

        ClimaResponse response = restTemplate.getForObject(url, ClimaResponse.class);
        return response;

    }



}
