package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.Restaurant;
import com.zosh.repository.CategoryRepository;
import com.zosh.service.CategoryServiceImplementation;
import com.zosh.service.RestaurantService;

public class CategoryServiceImplementationTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImplementation categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() throws RestaurantException {
        // Arrange
        String name = "Italian";
        Long userId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName(name);
        category.setRestaurant(restaurant);

        when(restaurantService.getRestaurantsByUserId(userId)).thenReturn(restaurant);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Category result = categoryService.createCategory(name, userId);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(restaurant, result.getRestaurant());

        verify(restaurantService, times(1)).getRestaurantsByUserId(userId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindCategoryByRestaurantId() throws RestaurantException {
        // Arrange
        Long restaurantId = 1L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Italian");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Mexican");

        List<Category> categories = Arrays.asList(category1, category2);

        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(categoryRepository.findByRestaurantId(restaurantId)).thenReturn(categories);

        // Act
        List<Category> result = categoryService.findCategoryByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Italian", result.get(0).getName());
        assertEquals("Mexican", result.get(1).getName());

        verify(restaurantService, times(1)).findRestaurantById(restaurantId);
        verify(categoryRepository, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testFindCategoryById() throws RestaurantException {
        // Arrange
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Italian");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.findCategoryById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());
        assertEquals("Italian", result.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testFindCategoryById_CategoryNotFound() {
        // Arrange
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RestaurantException exception = assertThrows(RestaurantException.class, () -> {
            categoryService.findCategoryById(categoryId);
        });

        assertEquals("category not exist with id " + categoryId, exception.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    // Additional tests for other methods and edge cases can be added similarly
}

