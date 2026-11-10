package com.github.ggarabs.user_service.adapters.inbound.dto;

import com.github.ggarabs.user_service.adapters.outbound.UserRole;

public record UpdateUserRoleRequest(UserRole role) {
}
