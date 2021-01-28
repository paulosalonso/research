package com.github.paulosalonso.research.application.security;

import com.github.paulosalonso.research.application.security.jwtconverter.JwtGrantedAuthoritiesConverterComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    private final JwtGrantedAuthoritiesConverterComposite jwtGrantedAuthoritiesConverterComposite;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    @Override
    public void configure(WebSecurity web) {
        permitAllForSwagger(web);
    }

    private void permitAllForSwagger(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs",
                "/swagger-ui/**",
                "/swagger-resources/**");
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverterComposite);
        return jwtAuthenticationConverter;
    }

}
