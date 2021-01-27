package com.github.paulosalonso.research.application.security.jwtconverter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Primary
@Component
public class JwtGrantedAuthoritiesConverterComposite implements Converter<Jwt, Collection<GrantedAuthority>> {

	private final List<Converter<Jwt,Collection<GrantedAuthority>>> converters;
	
	@Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {	
		return converters.stream()
				.map(converter -> converter.convert(jwt))
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
}
