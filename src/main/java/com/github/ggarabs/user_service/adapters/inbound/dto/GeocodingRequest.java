package com.github.ggarabs.user_service.adapters.inbound.dto;

// Exemplo de JSON: {"address": "Rua da Consolacao, 930, Sao Paulo, SP, Brasil"}
public record GeocodingRequest(String address) {
}
