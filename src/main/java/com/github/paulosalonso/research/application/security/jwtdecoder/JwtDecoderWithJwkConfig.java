package com.github.paulosalonso.research.application.security.jwtdecoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Slf4j
@Profile("jwk")
@Configuration
public class JwtDecoderWithJwkConfig {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${security.jwt.signature.jwk-set-uri}") String jwkSetUri) {
        log.info("Decode JWT using JWK on URI {}", jwkSetUri);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
