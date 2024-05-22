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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
    public void testResetPassword() throws UserException {
        // Given
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("dummy_token");
        request.setPassword("new_password");
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(new User());
        // Set a future expiration date to simulate an active token
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000)); // Set expiration 1 hour in the future
        when(passwordResetTokenService.findByToken("dummy_token")).thenReturn(resetToken);
        doNothing().when(userService).updatePassword(any(User.class), eq("new_password"));

        // When
        ResponseEntity<ApiResponse> response = resetPasswordController.resetPassword(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully.", response.getBody().getMessage());
        assertEquals(true, response.getBody().isStatus());
    }

    @Test
    public void testResetPassword_ExpiredToken() throws UserException {
        // Given
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("dummy_token");
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(new User());
        // Set a past expiration date to simulate an expired token
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() - 3600000)); // Set expiration 1 hour in the past
        when(passwordResetTokenService.findByToken("dummy_token")).thenReturn(resetToken);

        // When/Then
        UserException exception = org.junit.jupiter.api.Assertions.assertThrows(UserException.class, () -> {
            resetPasswordController.resetPassword(request);
        });
        assertEquals("token get expired...", exception.getMessage());
    }

    // Other test cases remain unchanged
}
