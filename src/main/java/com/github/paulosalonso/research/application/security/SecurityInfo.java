package com.github.paulosalonso.research.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.github.paulosalonso.research.application.security.Claims.TENANT;

@Component
public class SecurityInfo {

	public String getTenant() {
		return getClaim(TENANT)
				.map(Object::toString)
				.orElseThrow(TenantNotProvidedException::new);
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private Jwt getPrincipal() {
		return (Jwt) getAuthentication().getPrincipal();
	}

	private <T> Optional<T> getClaim(String claim) {
		return Optional.ofNullable((T) getPrincipal().getClaims().get(claim));
	}

}
