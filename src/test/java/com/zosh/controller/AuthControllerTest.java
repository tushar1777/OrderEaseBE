package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zosh.Exception.UserException;
import com.zosh.config.JwtProvider;
import com.zosh.domain.USER_ROLE;
import com.zosh.model.Cart;
import com.zosh.model.PasswordResetToken;
import com.zosh.model.User;
import com.zosh.repository.CartRepository;
import com.zosh.repository.UserRepository;
import com.zosh.request.LoginRequest;
import com.zosh.request.ResetPasswordRequest;
import com.zosh.response.ApiResponse;
import com.zosh.response.AuthResponse;
import com.zosh.service.CustomeUserServiceImplementation;
import com.zosh.service.PasswordResetTokenService;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private CustomeUserServiceImplementation customUserDetails;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testCreateUserHandler() throws UserException {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFullName("Test User");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwtToken");

        // When
        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSignin() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(any(Authentication.class))).thenReturn("jwtToken");

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        when(userDetails.getAuthorities()).thenReturn((Collection) authorities); // Stub getAuthorities()

        // When
        ResponseEntity<AuthResponse> response = authController.signin(loginRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    void testSigninWithInvalidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())).thenReturn(false);

        // When / Then
        try {
            authController.signin(loginRequest);
        } catch (BadCredentialsException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    void testResetPassword() throws UserException {
        // Given
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("token");
        resetPasswordRequest.setPassword("newPassword");

        PasswordResetToken resetToken = new PasswordResetToken();
        User user = new User();
        resetToken.setUser(user);
        // Set the expiry date to a future date to simulate a valid token
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 10000)); // 10 seconds in the future

        when(passwordResetTokenService.findByToken(resetPasswordRequest.getToken())).thenReturn(resetToken);

        // Mocking userService.updatePassword and passwordResetTokenService.delete
        doNothing().when(userService).updatePassword(any(User.class), anyString());
        doNothing().when(passwordResetTokenService).delete(any(PasswordResetToken.class));

        // When
        ResponseEntity<ApiResponse> response = authController.resetPassword(resetPasswordRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    void testResetPasswordWithExpiredToken() throws UserException {
        // Given
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("token");
        resetPasswordRequest.setPassword("newPassword");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(new User());

        // Set the expiry date in the past to simulate an expired token
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() - 1000)); // 1 second ago

        when(passwordResetTokenService.findByToken(resetPasswordRequest.getToken())).thenReturn(resetToken);

        // When / Then
        UserException exception = assertThrows(UserException.class, () -> {
            authController.resetPassword(resetPasswordRequest);
        });

        assertEquals("token get expired...", exception.getMessage());
    }


    @Test
    void testResetPasswordRequest() throws UserException {
        // Given
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userService.findUserByEmail(email)).thenReturn(user);

        // When
        ResponseEntity<ApiResponse> response = authController.resetPassword(email);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAuthenticateWithInvalidUsername() {
        // Given
        String username = "invalid@example.com";
        String password = "password";

        when(customUserDetails.loadUserByUsername(username)).thenReturn(null);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);

        // When / Then
        try {
            authController.signin(loginRequest);
        } catch (BadCredentialsException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    void testResetPasswordWithExpiredTokenTwo() throws UserException {
        // Given
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("token");
        resetPasswordRequest.setPassword("newPassword");

        when(passwordResetTokenService.findByToken(resetPasswordRequest.getToken())).thenReturn(null);

        // When / Then
        try {
            authController.resetPassword(resetPasswordRequest);
            fail("Expected UserException");
        } catch (UserException e) {
            assertEquals("token is required...", e.getMessage());
        }
    }

    @Test
    void testCreateUserHandler_EmailAlreadyExists() {
        // Given
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userRepository.findByEmail(any())).thenReturn(existingUser);

        User newUser = new User();
        newUser.setEmail("existing@example.com"); // Using the same email as the existing user

        // When / Then
        try {
            authController.createUserHandler(newUser);
            fail("Expected UserException");
        } catch (UserException e) {
            assertEquals("Email Is Already Used With Another Account", e.getMessage());
        }
    }

    @Test
    void testResetPassword_UserNotFound() throws UserException {
        // Given
        String email = "nonexistent@example.com";
        when(userService.findUserByEmail(anyString())).thenReturn(null);

        // When / Then
        try {
            authController.resetPassword(email);
            fail("Expected UserException");
        } catch (UserException e) {
            assertEquals("user not found", e.getMessage());
        }
    }

    @Test
    void testResetPassword_TokenNotFound() {
        // Given
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("nonexistent-token");
        when(passwordResetTokenService.findByToken(anyString())).thenReturn(null);

        // When / Then
        try {
            authController.resetPassword(resetPasswordRequest);
            fail("Expected UserException");
        } catch (UserException e) {
            assertEquals("token is required...", e.getMessage());
        }
    }

}
