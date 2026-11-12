package com.github.ggarabs.user_service.adapters.inbound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve bloquear acesso não autenticado às rotas de admin")
    void deveBloquearUsuarioNaoAutenticado() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve negar acesso (403) para usuário comum na rota admin")
    void deveNegarAcessoUsuarioComum() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }
}