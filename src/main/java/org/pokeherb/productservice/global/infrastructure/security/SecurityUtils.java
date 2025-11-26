package org.pokeherb.productservice.global.infrastructure.security;

public interface SecurityUtils {

    boolean isPermitted(String role);

    String getCurrentUsername();
}
