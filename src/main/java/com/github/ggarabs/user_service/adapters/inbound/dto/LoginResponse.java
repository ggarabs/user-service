package com.github.ggarabs.user_service.adapters.inbound.dto;

import java.util.UUID;

public record LoginResponse(UUID id, String email, String role, String message) {
}
