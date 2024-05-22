package com.zosh.controller;

import com.zosh.model.User;
import com.zosh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SupperAdminControllerTest {

    @InjectMocks
    private SupperAdminController supperAdminController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers() {
        // Given
        List<User> dummyUsers = new ArrayList<>();
        dummyUsers.add(new User());
        dummyUsers.add(new User());
        when(userService.findAllUsers()).thenReturn(dummyUsers);

        // When
        ResponseEntity<List<User>> response = supperAdminController.getAllCustomers();

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetPenddingRestaurantUser() {
        // Given
        List<User> dummyPendingUsers = new ArrayList<>();
        dummyPendingUsers.add(new User());
        dummyPendingUsers.add(new User());
        when(userService.getPenddingRestaurantOwner()).thenReturn(dummyPendingUsers);

        // When
        ResponseEntity<List<User>> response = supperAdminController.getPenddingRestaurantUser();

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}
