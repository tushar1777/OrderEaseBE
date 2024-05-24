package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.RestaurantException;
import com.zosh.dto.RestaurantDto;
import com.zosh.model.Address;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.RestaurantRepository;
import com.zosh.repository.UserRepository;
import com.zosh.request.CreateRestaurantRequest;

public class RestaurantServiceImplementationTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantServiceImplementation restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testCreateRestaurant() {
//        // Arrange
//        CreateRestaurantRequest request = new CreateRestaurantRequest();
//        Address address = new Address();
//        User user = new User();
//        when(addressRepository.save(any(Address.class))).thenReturn(address);
//        when(userRepository.save(any(User.class))).thenReturn(user);
//
//        // Act
//        Restaurant result = restaurantService.createRestaurant(request, user);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(user, result.getOwner());
//    }

    @Test
    void testFindRestaurantById() throws RestaurantException {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        Restaurant result = restaurantService.findRestaurantById(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
    }

    // Add more test methods as needed for other service methods
}

