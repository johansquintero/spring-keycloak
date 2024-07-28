package com.api.rest.service;

import com.api.rest.dto.UserDto;
import com.api.rest.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloackServiceImpl implements IKeycloackService{

    /**
     * metodo para listar todos los usuarios de keycloack
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUser() {
        return KeycloakProvider.getUserResource().list();
    }

    /**
     * metodo para buscar un usuario de keycloack por su username
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserName(String username) {
        return KeycloakProvider.getUserResource().search(username,true);
    }

    /**
     * metodo para crear un usuario de keycloack
     * @return String
     */
    @Override
    public String createUser(@NonNull UserDto userDto) {
        int status = 0;

        UsersResource usersResource = KeycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDto.firstName());
        userRepresentation.setLastName(userDto.lastName());
        userRepresentation.setUsername(userDto.username());
        userRepresentation.setEmail(userDto.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response=usersResource.create(userRepresentation);
        status = response.getStatus();
        if (status==201){
            String path  = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/")+1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDto.password());
            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();
            List<RoleRepresentation> roleRepresentations;
            if (userDto.roles()==null|| userDto.roles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
            }else{
                roleRepresentations = realmResource.roles().list().stream().filter(
                        roleRepresentation -> userDto.roles().stream().anyMatch(
                                roleName -> roleName.equalsIgnoreCase(roleRepresentation.getName())
                        )
                ).toList();
            }
            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);
        }else if (status==409){
            log.info("User already exists!!");
            return "User already exists!!";
        }else {
            log.info("Error creating user, pleas contact with the administrator!!");
            return "Error creating user, pleas contact with the administrator!!";
        }

        return "User created successfully!!";
    }

    /**
     * metodo para eliminar un usuario de keycloack
     */
    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource().get(userId).remove();
    }

    /**
     * metodo para actualizar un usuario de keycloack
     */
    @Override
    public void updateUser(String userId,@NonNull UserDto userDto) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(userDto.password());


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDto.firstName());
        userRepresentation.setLastName(userDto.lastName());
        userRepresentation.setUsername(userDto.username());
        userRepresentation.setEmail(userDto.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }
}
