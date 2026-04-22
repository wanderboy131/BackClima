package com.example.sistemadeclima;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://wanderboy131.github.io/")
@RestController
public class ClimaController {

    private final ClimaService climaService;
    private final LluviaService lluviaService;

    public ClimaController(ClimaService climaService, LluviaService lluviaService) {
        this.climaService = climaService;
        this.lluviaService = lluviaService;
    }

    @GetMapping("/traerProbabilidades")
    public String getClima(@RequestParam double latitud, @RequestParam double longitud){
        ClimaResponse clima = climaService.getClima(latitud, longitud);
        return lluviaService.estimarLluvia(clima);
    }
}
