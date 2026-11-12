package com.github.ggarabs.user_service.adapters.inbound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GeocodingServiceTest {

    private GeocodingService geocodingService;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        geocodingService = new GeocodingService(builder);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (400 BAD_REQUEST) quando endereço for inválido")
    void deveTratarEnderecoInvalido() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            geocodingService.buscarEndereco("");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar JsonNode com coordenadas quando endereço for válido")
    void deveBuscarEnderecoValido() {
        String jsonResposta = """
                [{"lat": "-23.5475", "lon": "-46.6361", "display_name": "São Paulo, Brasil"}]
                """;

        mockServer.expect(requestTo("https://nominatim.openstreetmap.org/search?q=Avenida%20Paulista,%201000&format=jsonv2&limit=1"))
                .andRespond(withSuccess(jsonResposta, MediaType.APPLICATION_JSON));

        JsonNode resultado = geocodingService.buscarEndereco("Avenida Paulista, 1000");

        assertNotNull(resultado);
        assertEquals("-23.5475", resultado.get(0).get("lat").asString());
        mockServer.verify();
    }
}