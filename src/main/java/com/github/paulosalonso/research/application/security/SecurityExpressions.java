package com.github.paulosalonso.research.application.security;

public final class SecurityExpressions {
    private SecurityExpressions() {}

    public static final String IS_ADMIN = "hasAuthority('ADMIN')";
}
