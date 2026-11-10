package com.github.ggarabs.user_service.adapters.inbound.dto;

// Contrato da nossa API; nao representa diretamente o JSON retornado pelo Google.
public record GeocodingResponse(
        Double latitude,
        Double longitude,
        String formattedAddress) {
}
