package com.github.paulosalonso.research.application.security.jwtconverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ClientScopesJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		var scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		return scopesAuthoritiesConverter.convert(jwt);
	}
}
