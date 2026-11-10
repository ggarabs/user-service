package com.github.ggarabs.user_service.adapters.inbound;

import com.github.ggarabs.user_service.adapters.inbound.dto.GeocodingRequest;
import com.github.ggarabs.user_service.adapters.inbound.dto.GeocodingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    private final GeocodingService geocodingService;

    public GeocodingController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @PostMapping
    public GeocodingResponse geocode(@RequestBody GeocodingRequest request) {
        if (request.address() == null || request.address().isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Não foi possível localizar o endereço"
            );
        }

        var result = geocodingService.buscarEndereco(request.address());

        if (result == null || !result.isArray()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Não foi possível obter as coordenadas do endereço"
            );
        }

        if (result.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Nenhum endereço encontrado"
            );
        }

        var primeiro = result.get(0);
        try {
            double latitude = Double.parseDouble(primeiro.path("lat").asText());
            double longitude = Double.parseDouble(primeiro.path("lon").asText());
            String enderecoFormatado = primeiro.path("display_name").asText();
            if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
                    || Math.abs(latitude) > 90 || Math.abs(longitude) > 180
                    || enderecoFormatado.isBlank()) {
                throw new IllegalArgumentException("Dados de geocodificação inválidos");
            }
            return new GeocodingResponse(latitude, longitude, enderecoFormatado);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "O serviço de geocodificação retornou dados inválidos",
                exception
            );
        }
    }
}
