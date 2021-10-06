package com.rbkmoney.anapi.v2.service;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class KeycloakService {

    public AccessToken getAccessToken() {
        var keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return keycloakPrincipal.getKeycloakSecurityContext().getToken();
    }
}
