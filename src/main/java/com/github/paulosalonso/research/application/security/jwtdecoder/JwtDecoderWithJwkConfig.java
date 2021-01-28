package com.github.paulosalonso.research.application.security.jwtdecoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Profile("jwk")
@Configuration
public class JwtDecoderWithJwkConfig {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${security.jwt.signature.jkw-set-uri}") String jwkSetUri) {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
