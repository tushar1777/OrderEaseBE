package com.zosh.controller;

import com.zosh.Exception.UserException;
import com.zosh.model.PasswordResetToken;
import com.zosh.model.User;
import com.zosh.request.ResetPasswordRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.PasswordResetTokenService;
import com.zosh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ResetPasswordControllerTest {

    @InjectMocks
    private ResetPasswordController resetPasswordController;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testResetPasswordTokenNotFound() {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("invalid-token");
        request.setPassword("newPassword");

        when(passwordResetTokenService.findByToken(anyString())).thenReturn(null);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            resetPasswordController.resetPassword(request);
        });

        assertEquals("token is required...", exception.getMessage());
        verify(passwordResetTokenService, times(1)).findByToken(request.getToken());
    }

    @Test
    public void testResetPasswordTokenExpired() {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("expired-token");
        request.setPassword("newPassword");

        PasswordResetToken token = mock(PasswordResetToken.class);
        when(token.isExpired()).thenReturn(true);
        when(passwordResetTokenService.findByToken(anyString())).thenReturn(token);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            resetPasswordController.resetPassword(request);
        });

        assertEquals("token get expired...", exception.getMessage());
        verify(passwordResetTokenService, times(1)).findByToken(request.getToken());
        verify(passwordResetTokenService, times(1)).delete(token);
    }

    @Test
    public void testResetPasswordSuccess() throws UserException {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("valid-token");
        request.setPassword("newPassword");

        User user = new User();
        PasswordResetToken token = mock(PasswordResetToken.class);
        when(token.isExpired()).thenReturn(false);
        when(token.getUser()).thenReturn(user);
        when(passwordResetTokenService.findByToken(anyString())).thenReturn(token);

        // Act
        ResponseEntity<ApiResponse> response = resetPasswordController.resetPassword(request);

        // Assert
        assertEquals("Password updated successfully.", response.getBody().getMessage());
        assertEquals(true, response.getBody().isStatus());
        verify(passwordResetTokenService, times(1)).findByToken(request.getToken());
        verify(userService, times(1)).updatePassword(user, request.getPassword());
        verify(passwordResetTokenService, times(1)).delete(token);
    }

    @Test
    public void testResetPasswordEmailUserNotFound() throws UserException {
        // Arrange
        String email = "user@example.com";

        when(userService.findUserByEmail(anyString())).thenReturn(null);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            resetPasswordController.resetPassword(email);
        });

        assertEquals("user not found", exception.getMessage());
        verify(userService, times(1)).findUserByEmail(email);
    }

    @Test
    public void testResetPasswordEmailSuccess() throws UserException {
        // Arrange
        String email = "user@example.com";
        User user = new User();

        when(userService.findUserByEmail(anyString())).thenReturn(user);

        // Act
        ResponseEntity<ApiResponse> response = resetPasswordController.resetPassword(email);

        // Assert
        assertEquals("Password reset email sent successfully.", response.getBody().getMessage());
        assertEquals(true, response.getBody().isStatus());
        verify(userService, times(1)).findUserByEmail(email);
        verify(userService, times(1)).sendPasswordResetEmail(user);
    }
}
