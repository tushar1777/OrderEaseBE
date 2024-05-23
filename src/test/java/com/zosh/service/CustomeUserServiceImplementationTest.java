package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zosh.domain.USER_ROLE;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomeUserServiceImplementationTest {

    @InjectMocks
    private CustomeUserServiceImplementation userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
    }

    @Test
    public void testLoadUserByUsernameUserFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CUSTOMER", authorities.get(0).getAuthority());

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("test@example.com");
        });

        assertEquals("user not found with email  - test@example.com", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testLoadUserByUsernameUserRoleNull() {
        user.setRole(null);
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        assertEquals(1, authorities.size());
        assertEquals("ROLE_CUSTOMER", authorities.get(0).getAuthority());

        verify(userRepository, times(1)).findByEmail(anyString());
    }
}
