package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zosh.domain.USER_ROLE;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import com.zosh.service.CustomeUserServiceImplementation;

public class CustomeUserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomeUserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        String email = "user@example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(USER_ROLE.ROLE_ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        List<GrantedAuthority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_ADMIN.toString()));

        assertEquals(expectedAuthorities.size(), userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().containsAll(expectedAuthorities));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        // Arrange
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(email);
        });

        assertEquals("user not found with email  - " + email, exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserHasNoRole() {
        // Arrange
        String email = "user@example.com";
        String password = "password";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(null); // User has no role

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        List<GrantedAuthority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString())); // Default role

        assertEquals(expectedAuthorities.size(), userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().containsAll(expectedAuthorities));

        verify(userRepository, times(1)).findByEmail(email);
    }

    // Additional tests for other edge cases can be added similarly
}

