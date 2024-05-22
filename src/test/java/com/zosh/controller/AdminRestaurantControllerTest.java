package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.request.CreateRestaurantRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.RestaurantService;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
class AdminRestaurantControllerTest {

    @Mock
    RestaurantService restaurantService;

    @Mock
    UserService userService;

    @InjectMocks
    AdminRestaurantController adminRestaurantController;

    @Test
    void testCreateRestaurant() throws UserException {
        // Given
        String jwt = "sample-jwt";
        CreateRestaurantRequest req = new CreateRestaurantRequest();
        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(restaurantService.createRestaurant(req, user)).thenReturn(restaurant);

        // When
        ResponseEntity<Restaurant> response = adminRestaurantController.createRestaurant(req, jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateRestaurant() throws RestaurantException, UserException {
        // Given
        String jwt = "sample-jwt";
        Long id = 123L;
        CreateRestaurantRequest req = new CreateRestaurantRequest();
        User user = new User();
        Restaurant restaurant = new Restaurant();
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(restaurantService.updateRestaurant(id, req)).thenReturn(restaurant);

        // When
        ResponseEntity<Restaurant> response = adminRestaurantController.updateRestaurant(id, req, jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteRestaurantById() throws RestaurantException, UserException {
        // Given
        String jwt = "sample-jwt";
        Long restaurantId = 123L;
        User user = new User();
        ApiResponse apiResponse = new ApiResponse("Restaurant Deleted with id Successfully", true);
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        doNothing().when(restaurantService).deleteRestaurant(restaurantId);

        // When
        ResponseEntity<ApiResponse> response = adminRestaurantController.deleteRestaurantById(restaurantId, jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateRestaurantStatus() throws RestaurantException, UserException {
        // Given
        String jwt = "sample-jwt";
        Long id = 123L;
        User user = new User();
        Restaurant restaurant = new Restaurant();

        when(restaurantService.updateRestaurantStatus(id)).thenReturn(restaurant);

        // When
        ResponseEntity<Restaurant> response = adminRestaurantController.updateStataurantStatus(jwt, id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindRestaurantByUserId() throws RestaurantException, UserException {
        // Given
        String jwt = "sample-jwt";
        User user = new User();
        user.setId(123L);
        Restaurant restaurant = new Restaurant();
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(restaurantService.getRestaurantsByUserId(user.getId())).thenReturn(restaurant);

        // When
        ResponseEntity<Restaurant> response = adminRestaurantController.findRestaurantByUserId(jwt);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
