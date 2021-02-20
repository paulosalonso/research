package com.github.paulosalonso.research.application.security;

public class TenantNotProvidedException extends RuntimeException {
    public TenantNotProvidedException() {
        super("The JWT does not contain the tenant claim");
    }
}
