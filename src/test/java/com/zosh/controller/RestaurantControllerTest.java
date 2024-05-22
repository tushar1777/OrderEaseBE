package com.zosh.controller;

import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.dto.RestaurantDto;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.service.RestaurantService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class RestaurantControllerTest {

    @InjectMocks
    private RestaurantController restaurantController;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindRestaurantByName() {
        // Given
        String keyword = "test";
        List<Restaurant> restaurants = new ArrayList<>();
        when(restaurantService.searchRestaurant(keyword)).thenReturn(restaurants);

        // When
        ResponseEntity<List<Restaurant>> response = restaurantController.findRestaurantByName(keyword);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurants, response.getBody());
    }

    @Test
    public void testGetAllRestaurants() {
        // Given
        List<Restaurant> restaurants = new ArrayList<>();
        when(restaurantService.getAllRestaurant()).thenReturn(restaurants);

        // When
        ResponseEntity<List<Restaurant>> response = restaurantController.getAllRestaurants();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurants, response.getBody());
    }

    @Test
    public void testFindRestaurantById() throws RestaurantException {
        // Given
        Long id = 1L;
        Restaurant restaurant = new Restaurant();
        when(restaurantService.findRestaurantById(id)).thenReturn(restaurant);

        // When
        ResponseEntity<Restaurant> response = restaurantController.findRestaurantById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurant, response.getBody());
    }

    @Test
    public void testAddToFavorite() throws RestaurantException, UserException {
        // Given
        Long id = 1L;
        String jwt = "dummy_jwt";
        User user = new User();
        RestaurantDto restaurantDto = new RestaurantDto();
        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(restaurantService.addToFavorites(eq(id), any(User.class))).thenReturn(restaurantDto);

        // When
        ResponseEntity<RestaurantDto> response = restaurantController.addToFavorite(jwt, id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurantDto, response.getBody());
    }
}
