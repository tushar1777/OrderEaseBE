package com.zosh.controller;

import com.zosh.model.Category;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.request.CreateFoodRequest;
import com.zosh.service.CategoryService;
import com.zosh.service.FoodService;
import com.zosh.service.RestaurantService;
import com.zosh.service.UserService;
import org.apache.coyote.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminMenuItemControllerTest {

    @InjectMocks
    private AdminMenuItemController adminMenuItemController;

    @Mock
    private FoodService menuItemService;

    @Mock
    private UserService userService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CreateFoodRequest request;

    @Test
    public void testCreateItem() throws Exception {
        // Arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        CreateFoodRequest request = new CreateFoodRequest();
        request.setName("Test Food");
        request.setPrice(100L);
        request.setRestaurantId(1L);

        Category category = new Category();
        category.setName("Test Category");
        category.setRestaurant(restaurant);

        User user = new User();
        user.setId(1L);
        user.setFullName("Test User");

        Food food = new Food();
        food.setId(1L);
        food.setName("Test Food");

        ResponseEntity<Food> result = adminMenuItemController.createItem(request, "jwt");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }


    @Test
    public void testDeleteItem() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setFullName("Test User");

        when(userService.findUserProfileByJwt(any(String.class))).thenReturn(user);
        doNothing().when(menuItemService).deleteFood(anyLong());

        // Act
        ResponseEntity<String> response = adminMenuItemController.deleteItem(1L, "jwt");

        // Assert
        assertEquals("Menu item deleted", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testGetMenuItemByName() {
        // Arrange
        Food food = new Food();
        food.setId(1L);
        food.setName("Test Food");

        when(menuItemService.searchFood(anyString())).thenReturn(Arrays.asList(food));

        // Act
        ResponseEntity<List<Food>> response = adminMenuItemController.getMenuItemByName("Test Food");

        // Assert
        List<Food> result = response.getBody();
        assertEquals(1, result.size());
        assertEquals(food, result.get(0));
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateAvilibilityStatus() throws Exception {
        // Arrange
        Food food = new Food();
        food.setId(1L);
        food.setName("Test Food");
        food.setAvailable(false);

        when(menuItemService.updateAvailibilityStatus(anyLong())).thenReturn(food);

        // Act
        ResponseEntity<Food> response = adminMenuItemController.updateAvilibilityStatus(1L);

        // Assert
        assertEquals(food, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }
}
