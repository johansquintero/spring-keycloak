package com.api.rest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/test")
public class TestController {

    @GetMapping(path = "/hello-1")
    @PreAuthorize("hasRole('admin_client_role')")
    public String helloAdmin(){
        return "Hello Spring Boot with Keycloack - ADMIN";
    }

    @GetMapping(path = "/hello-2")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public String helloUser(){
        return "Hello Spring Boot with Keycloack - USER";
    }
}
