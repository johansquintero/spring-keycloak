package com.api.rest.controllers;

import com.api.rest.dto.UserDto;
import com.api.rest.service.IKeycloackService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/keycloack/user")
@AllArgsConstructor
@PreAuthorize("hasRole('admin_client_role')")
public class KeycloackController {
    private final IKeycloackService keycloackService;


    @GetMapping
    public ResponseEntity<List<UserRepresentation>> findAllUsers(){
        return ResponseEntity.ok(this.keycloackService.findAllUser());
    }

    @GetMapping("/find-user/{username}")
    public ResponseEntity<List<UserRepresentation>> findUserByUsername(@PathVariable(name = "username") String username){
        return ResponseEntity.ok(this.keycloackService.searchUserName(username));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/api/keycloack/user/create"))
                .body(this.keycloackService.createUser(userDto));
    }

    @PutMapping(path = "/update/{user-id}")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto,@PathVariable(name = "user-id") String userId){
        this.keycloackService.updateUser(userId,userDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping(path = "/delete/{user-id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "user-id") String userId){
        this.keycloackService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
