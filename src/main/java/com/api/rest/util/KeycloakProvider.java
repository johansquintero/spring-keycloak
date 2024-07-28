package com.api.rest.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProvider {
    private final static String SERVER_URL = "http://localhost:9090";
    private final static String REALM_NAME =  "spring-boot-realm-dev";
    private final static String REALM_MASTER =  "master";
    private final static String ADMIN_CLI =  "admin-cli";
    private final static String USER_CONSOLE =  "johansquintero";
    private final static String USER_PASSWORD =  "12345";
    private final static String CLIENT_SECRED =  "i4vJkA1Iewe9npLrkCmfvnFykY0Lk0fP";

    public static RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(USER_PASSWORD)
                .clientSecret(CLIENT_SECRED)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build()
                )
                .build();
        return keycloak.realm(REALM_NAME);
    }

    public static UsersResource getUserResource(){
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }
}
