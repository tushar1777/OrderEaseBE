package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zosh.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.model.Notification;
import com.zosh.model.Order;
import com.zosh.model.User;
import com.zosh.repository.NotificationRepository;

public class NotificationServiceImplementationTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImplementation notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendOrderStatusNotification() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus("Processed");
        User customer = new User();
        order.setCustomer(customer);

        Notification notification = new Notification();
        notification.setMessage("your order is Processed order id is - 1");
        notification.setCustomer(customer);
        notification.setSentAt(new Date());

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        Notification result = notificationService.sendOrderStatusNotification(order);

        // Assert
        assertNotNull(result);
        assertEquals("your order is Processed order id is - 1", result.getMessage());

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testFindUsersNotification() {
        // Arrange
        Long userId = 1L;
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());

        when(notificationRepository.findByCustomerId(userId)).thenReturn(notifications);

        // Act
        List<Notification> result = notificationService.findUsersNotification(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(notificationRepository, times(1)).findByCustomerId(userId);

        Restaurant restaurant = new Restaurant();
        String message = "Message";
        User user = new User();
        notificationService.sendRestaurantNotification(restaurant,message);
        notificationService.sendPromotionalNotification(user,message);
    }

    // Additional test methods for the unimplemented methods can be added similarly
}
