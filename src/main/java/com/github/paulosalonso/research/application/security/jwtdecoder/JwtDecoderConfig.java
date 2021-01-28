package com.github.paulosalonso.research.application.security.jwtdecoder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Profile("default")
@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${security.jwt.signature.secret}") String secret,
            @Value("${security.jwt.signature.algorithm:HmacSHA256}") String algorithm) {

        log.info("Decode JWT using secret key");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), algorithm);
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
