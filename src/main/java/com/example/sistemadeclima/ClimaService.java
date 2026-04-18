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

        String latStr = String.format("%.2f", latitud).replace(",", ".").trim();
        String lonStr = String.format("%.2f", longitud).replace(",", ".").trim();

        System.out.println("lat: '" + latStr + "'");
        System.out.println("lon: '" + lonStr + "'");
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latStr + "&longitude=" + lonStr + "&current=temperature_2m,relative_humidity_2m,weather_code,cloud_cover,surface_pressure,precipitation";
        System.out.println(url);
        ClimaResponse response = restTemplate.getForObject(url, ClimaResponse.class);
        return response;

    }



}
