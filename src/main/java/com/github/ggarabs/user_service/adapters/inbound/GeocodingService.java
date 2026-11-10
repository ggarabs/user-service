package com.github.ggarabs.user_service.adapters.inbound;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import org.springframework.web.server.ResponseStatusException;


@Service
public class GeocodingService {
    private final RestClient client;

    public GeocodingService(RestClient.Builder builder) {
        this.client = builder
        .baseUrl("https://nominatim.openstreetmap.org")
        .defaultHeader(
                "User-Agent",
                "ProjetoFaculdade/1.0 (contato: seu-email@exemplo.com)"
        )
        .build();
    }

    public JsonNode buscarEndereco(String address) {
        if (EnderecoValidar.validar(address)) {
            return client.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/search")
                    .queryParam("q", address)
                    .queryParam("format", "jsonv2")
                    .queryParam("limit", 1)
                    .build())
                .retrieve()
                .body(JsonNode.class);
        } 

        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "O endereço não é valido"
        );
    }
}
