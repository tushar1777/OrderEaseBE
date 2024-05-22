package com.zosh.controller;

import com.stripe.exception.StripeException;
import com.zosh.Exception.*;
import com.zosh.model.*;
import com.zosh.request.CreateOrderRequest;
import com.zosh.service.OrderService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder_ValidOrder() throws UserException, RestaurantException, CartException, StripeException, OrderException {
        // Given
        CreateOrderRequest orderRequest = new CreateOrderRequest();
       // orderRequest.setUserId(1L);
        User user = new User();
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(orderService.createOrder(any(CreateOrderRequest.class), any(User.class))).thenReturn(new PaymentResponse());

        // When
        ResponseEntity<PaymentResponse> response = orderController.createOrder(orderRequest, "dummy_jwt");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

//    @Test
//    public void testCreateOrder_NullOrder() {
//        // When
//        OrderException exception = assertThrows(OrderException.class, () -> orderController.createOrder(null, "dummy_jwt"));
//
//        // Then
//        assertEquals("Please provide valid request body", exception.getMessage());
//    }

    @Test
    public void testGetAllUserOrders_ValidUser() throws OrderException, UserException {
        // Given
        User user = new User();
        user.setId(1L);
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(orderService.getUserOrders(1L)).thenReturn(new ArrayList<>());

        // When
        ResponseEntity<List<Order>> response = orderController.getAllUserOrders("dummy_jwt");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

//    @Test
//    public void testGetAllUserOrders_InvalidUser() throws OrderException, UserException {
//        // Given
//        when(userService.findUserProfileByJwt(anyString())).thenReturn(null);
//
//        // When
//        ResponseEntity<List<Order>> response = orderController.getAllUserOrders("dummy_jwt");
//
//        // Then
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertNull(response.getBody());
//    }
}
