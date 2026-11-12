package com.github.ggarabs.user_service.adapters.inbound;

import com.github.ggarabs.user_service.adapters.inbound.dto.LoginRequest;
import com.github.ggarabs.user_service.adapters.outbound.UserRepository;
import com.github.ggarabs.user_service.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Deve rejeitar login com payload vazio ou inválido")
    void deveRejeitarLoginInvalido() throws Exception {
        LoginRequest request = new LoginRequest("", "");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}