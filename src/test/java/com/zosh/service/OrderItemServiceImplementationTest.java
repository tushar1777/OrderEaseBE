package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.model.OrderItem;
import com.zosh.repository.OrderItemRepository;

public class OrderItemServiceImplementationTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderItem() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(3);

        OrderItem savedOrderItem = new OrderItem();
        savedOrderItem.setQuantity(3);

        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(savedOrderItem);

        // Act
        OrderItem result = orderItemService.createOrderIem(orderItem);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getQuantity());

        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }
}

