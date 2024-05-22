package com.zosh.controller;

import com.zosh.Exception.UserException;
import com.zosh.model.Notification;
import com.zosh.model.User;
import com.zosh.service.NotificationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindUsersNotification() throws UserException {
        // Given
        String jwt = "dummy_jwt";
        User user = new User();
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.findUsersNotification(anyLong())).thenReturn(notifications);

        // When
        ResponseEntity<List<Notification>> response = notificationController.findUsersNotification(jwt);

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(notifications, response.getBody());
    }

}
