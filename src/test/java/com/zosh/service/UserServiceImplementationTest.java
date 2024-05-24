//package com.zosh.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.zosh.Exception.UserException;
//import com.zosh.config.JwtProvider;
//import com.zosh.model.PasswordResetToken;
//import com.zosh.model.User;
//import com.zosh.repository.PasswordResetTokenRepository;
//import com.zosh.repository.UserRepository;
//
//public class UserServiceImplementationTest {
//
//    private UserRepository userRepository;
//    private JwtProvider jwtProvider;
//    private PasswordEncoder passwordEncoder;
//    private PasswordResetTokenRepository passwordResetTokenRepository;
//    private JavaMailSender javaMailSender;
//    private UserServiceImplementation userService;
//
//    @Before
//    public void setUp() {
//        userRepository = mock(UserRepository.class);
//        jwtProvider = mock(JwtProvider.class);
//        passwordEncoder = mock(PasswordEncoder.class);
//        passwordResetTokenRepository = mock(PasswordResetTokenRepository.class);
//        javaMailSender = mock(JavaMailSender.class);
//        userService = new UserServiceImplementation(userRepository, jwtProvider, passwordEncoder,
//                passwordResetTokenRepository, javaMailSender);
//    }
//
//    @Test
//    public void testFindUserProfileByJwt() throws UserException {
//        String jwt = "someJwt";
//        String email = "test@example.com";
//        User user = new User();
//        user.setEmail(email);
//
//        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
//        when(userRepository.findByEmail(email)).thenReturn(user);
//
//        User foundUser = userService.findUserProfileByJwt(jwt);
//
//        assertNotNull(foundUser);
//        assertEquals(email, foundUser.getEmail());
//    }
//
//    @Test(expected = UserException.class)
//    public void testFindUserProfileByJwt_UserNotFound() throws UserException {
//        String jwt = "someJwt";
//        String email = "test@example.com";
//
//        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
//        when(userRepository.findByEmail(email)).thenReturn(null);
//
//        userService.findUserProfileByJwt(jwt);
//    }
//
//    @Test
//    public void testFindAllUsers() throws UserException {
//        List<User> users = new ArrayList<>();
//        users.add(new User());
//        users.add(new User());
//
//        when(userRepository.findAll()).thenReturn(users);
//        when(userRepository.getPenddingRestaurantOwners()).thenReturn(users);
//        when(userRepository.findByEmail("username")).thenReturn(new User());
//
//        userService.getPenddingRestaurantOwner();
//        userService.updatePassword(new User(),"");
//        userService.sendPasswordResetEmail(new User());
//
//        List<User> foundUsers = userService.findAllUsers();
//
//        assertNotNull(foundUsers);
//        assertEquals(2, foundUsers.size());
//    }
//
//    // Add more test cases for other methods...
//
//    @Test
//    public void testFindUserByEmail_UserExists() throws UserException {
//        String email = "test@example.com";
//        User user = new User();
//        user.setEmail(email);
//
//        when(userRepository.findByEmail(email)).thenReturn(user);
//
//        User foundUser = userService.findUserByEmail(email);
//
//        assertNotNull(foundUser);
//        assertEquals(email, foundUser.getEmail());
//    }
//
//    @Test(expected = UserException.class)
//    public void testFindUserByEmail_UserNotFound() throws UserException {
//        String email = "test@example.com";
//
//        when(userRepository.findByEmail(email)).thenReturn(null);
//
//        userService.findUserByEmail(email);
//    }
//
//}
