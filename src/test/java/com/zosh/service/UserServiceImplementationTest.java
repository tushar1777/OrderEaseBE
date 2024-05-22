package com.zosh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zosh.Exception.UserException;
import com.zosh.config.JwtProvider;
import com.zosh.model.PasswordResetToken;
import com.zosh.model.User;
import com.zosh.repository.PasswordResetTokenRepository;
import com.zosh.repository.UserRepository;
import com.zosh.service.UserServiceImplementation;

public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserProfileByJwt() throws UserException {
        // Arrange
        String jwt = "some.jwt.token";
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.findUserProfileByJwt(jwt);

        // Assert
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.findAllUsers();

        // Assert
        assertEquals(userList, result);
    }

    @Test
    void testGetPendingRestaurantOwner() {
        // Arrange
        List<User> userList = new ArrayList<>();
        when(userRepository.getPenddingRestaurantOwners()).thenReturn(userList);

        // Act
        List<User> result = userService.getPenddingRestaurantOwner();

        // Assert
        assertEquals(userList, result);
    }

    @Test
    void testUpdatePassword() {
        // Arrange
        User user = new User();
        String newPassword = "newPassword";
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // Act
        userService.updatePassword(user, newPassword);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testFindUserByEmail() throws UserException {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.findUserByEmail(email);

        // Assert
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindUserByEmail_ThrowsExceptionWhenUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(UserException.class, () -> userService.findUserByEmail(email));
    }

//    @Test
//    void testSendPasswordResetEmail() {
//        // Arrange
//        User user = new User();
//        user.setEmail("user@example.com");
//
//        String resetToken = "resetToken";
//        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(new PasswordResetToken());
//        when(javaMailSender.send(any(SimpleMailMessage.class))).thenReturn(null);
//
//        // Act
//        userService.sendPasswordResetEmail(user);
//
//        // Assert
//        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
//        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
//    }
}

