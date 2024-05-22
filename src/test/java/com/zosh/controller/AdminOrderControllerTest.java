package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zosh.Exception.OrderException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Order;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
class AdminOrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    AdminOrderController adminOrderController;

    @Test
    void testDeleteOrder() throws OrderException {
        // Given
        Long orderId = 123L;
        doNothing().when(orderService).cancelOrder(orderId);

        // When
        ResponseEntity<String> response = adminOrderController.deleteOrder(orderId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteOrderWithNullOrderId() throws OrderException {
        // When
        ResponseEntity<String> response = adminOrderController.deleteOrder(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAllRestaurantOrders() throws OrderException, RestaurantException {
        // Given
        Long restaurantId = 456L;
        List<Order> orders = new ArrayList<>();
        when(orderService.getOrdersOfRestaurant(restaurantId, null)).thenReturn(orders);

        // When
        ResponseEntity<List<Order>> response = adminOrderController.getAllRestaurantOrders(restaurantId, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void testUpdateOrders() throws OrderException, RestaurantException {
        // Given
        Long orderId = 789L;
        String orderStatus = "NEW";
        Order order = new Order();
        when(orderService.updateOrder(orderId, orderStatus)).thenReturn(order);

        // When
        ResponseEntity<Order> response = adminOrderController.updateOrders(orderId, orderStatus);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }
}
