package com.github.ggarabs.user_service.adapters.inbound;

import com.github.ggarabs.user_service.adapters.inbound.dto.UpdateUserRoleRequest;
import com.github.ggarabs.user_service.adapters.outbound.UserEntity;
import com.github.ggarabs.user_service.adapters.outbound.UserRepository;
import com.github.ggarabs.user_service.adapters.outbound.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}/role")
    public UserRole updateRole(@PathVariable UUID id, @RequestBody UpdateUserRoleRequest request) {
        if (request == null || request.role() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O perfil é obrigatório");
        }

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        user.setRole(request.role());
        userRepository.save(user);
        return request.role();
    }
}
