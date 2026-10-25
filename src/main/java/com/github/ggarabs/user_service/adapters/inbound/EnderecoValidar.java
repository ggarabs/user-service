package com.github.ggarabs.user_service.adapters.inbound;
import java.util.regex.Pattern;

public class EnderecoValidar {
    private static final Pattern ENDERECO_PATTERN =
        Pattern.compile(
            "^[\\p{L}0-9][\\p{L}0-9\\s.,'ºª°/-]{4,149}$"
        );
    
    public static boolean validar(String endereco) {
        if(endereco == null) {
            return false;
        }

        return ENDERECO_PATTERN.matcher(endereco.trim()).matches();
    }
}
