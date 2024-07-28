package com.api.rest.service;

import com.api.rest.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloackService {
    List<UserRepresentation> findAllUser();

    List<UserRepresentation> searchUserName(String username);

    String createUser(UserDto userDto);

    void deleteUser(String userId);
    void updateUser(String userId,UserDto userDto);
}
