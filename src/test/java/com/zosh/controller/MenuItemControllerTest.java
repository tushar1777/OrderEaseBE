package com.zosh.controller;

import com.zosh.Exception.FoodException;
import com.zosh.model.Food;
import com.zosh.service.FoodService;
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

public class MenuItemControllerTest {

    @InjectMocks
    private MenuItemController menuItemController;

    @Mock
    private FoodService foodService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchFood() {
        // Given
        String name = "burger";
        List<Food> foods = new ArrayList<>();
        when(foodService.searchFood(name)).thenReturn(foods);

        // When
        ResponseEntity<List<Food>> response = menuItemController.searchFood(name);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(foods, response.getBody());
    }

    @Test
    public void testGetMenuItemByRestaurantId() throws FoodException {
        // Given
        Long restaurantId = 1L;
        boolean vegetarian = true;
        boolean seasonal = false;
        boolean nonveg = false;
        List<Food> menuItems = new ArrayList<>();
        when(foodService.getRestaurantsFood(anyLong(), anyBoolean(), anyBoolean(), anyBoolean(), anyString())).thenReturn(menuItems);

        // When
        ResponseEntity<List<Food>> response = menuItemController.getMenuItemByRestaurantId(restaurantId, vegetarian, seasonal, nonveg, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuItems, response.getBody());
    }
}
