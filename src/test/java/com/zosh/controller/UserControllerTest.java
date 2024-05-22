package com.zosh.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zosh.Exception.UserException;
import com.zosh.model.User;
import com.zosh.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testGetUserProfileHandler() throws UserException {
        // Given
        String jwt = "dummy_jwt";
        User dummyUser = new User();
     //   dummyUser.setUsername("testuser");
        when(userService.findUserProfileByJwt(anyString())).thenReturn(dummyUser);

        // When
        ResponseEntity<User> response = userController.getUserProfileHandler(jwt);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
       // assertEquals("testuser", response.getBody().getUsername());
        assertEquals(null, response.getBody().getPassword()); // Password should be null
    }
}
