package com.zosh.controller;

import com.zosh.Exception.UserException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.User;
import com.zosh.service.CategoryService;
import com.zosh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatedCategory() throws UserException, RestaurantException {
        // Arrange
        String jwt = "some-jwt-token";
        Category category = new Category();
        category.setName("Italian");

        User user = new User();
        user.setId(1L);

        Category createdCategory = new Category();
        createdCategory.setName("Italian");

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(categoryService.createCategory(anyString(), anyLong())).thenReturn(createdCategory);

        // Act
        ResponseEntity<Category> response = categoryController.createdCategory(jwt, category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdCategory, response.getBody());

        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(categoryService, times(1)).createCategory(category.getName(), user.getId());
    }

    @Test
    public void testGetRestaurantsCategory() throws UserException, RestaurantException {
        // Arrange
        String jwt = "some-jwt-token";
        Long restaurantId = 1L;

        User user = new User();
        user.setId(1L);

        Category category1 = new Category();
        category1.setName("Italian");

        Category category2 = new Category();
        category2.setName("Mexican");

        List<Category> categories = Arrays.asList(category1, category2);

        when(userService.findUserProfileByJwt(anyString())).thenReturn(user);
        when(categoryService.findCategoryByRestaurantId(anyLong())).thenReturn(categories);

        // Act
        ResponseEntity<List<Category>> response = categoryController.getRestaurantsCategory(restaurantId, jwt);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());

        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(categoryService, times(1)).findCategoryByRestaurantId(restaurantId);
    }
}
