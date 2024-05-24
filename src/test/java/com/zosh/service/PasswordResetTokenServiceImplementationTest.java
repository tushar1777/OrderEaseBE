package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.model.PasswordResetToken;
import com.zosh.repository.PasswordResetTokenRepository;

public class PasswordResetTokenServiceImplementationTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private PasswordResetTokenServiceImplementation passwordResetTokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testFindByToken() {
//        // Arrange
//        String token = "testToken";
//        PasswordResetToken passwordResetToken = new PasswordResetToken();
//        passwordResetToken.setToken(token);
//
//        when(passwordResetTokenRepository.findByToken(token)).thenReturn(passwordResetToken);
//
//        // Act
//        PasswordResetToken result = passwordResetTokenService.findByToken(token);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(token, result.getToken());
//
//        verify(passwordResetTokenRepository, times(1)).findByToken(token);
//    }

    @Test
    void testDelete() {
        // Arrange
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken("testToken");

        doNothing().when(passwordResetTokenRepository).delete(passwordResetToken);

        // Act
        passwordResetTokenService.delete(passwordResetToken);

        // Assert
        verify(passwordResetTokenRepository, times(1)).delete(passwordResetToken);
    }
}

