package com.zosh.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zosh.service.PasswordResetTokenServiceImplementation;
import org.junit.Before;
import org.junit.Test;

import com.zosh.model.PasswordResetToken;
import com.zosh.repository.PasswordResetTokenRepository;

public class PasswordResetTokenServiceImplementationTest {

    private PasswordResetTokenServiceImplementation passwordResetTokenService;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Before
    public void setUp() {
        passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
        passwordResetTokenService = new PasswordResetTokenServiceImplementation(passwordResetTokenRepository);
    }

    @Test
    public void testFindByToken() {
        String token = "someToken";
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);

        when(passwordResetTokenRepository.findByToken(token)).thenReturn(passwordResetToken);

        PasswordResetToken foundToken = passwordResetTokenService.findByToken(token);

        assertEquals(token, foundToken.getToken());
    }

    @Test
    public void testDelete() {
        PasswordResetToken resetToken = new PasswordResetToken();

        passwordResetTokenService.delete(resetToken);

        verify(passwordResetTokenRepository).delete(resetToken);
    }
}
