package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.Restaurant;
import com.zosh.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplementationTest {

    @InjectMocks
    private CategoryServiceImplementation categoryService;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private CategoryRepository categoryRepository;

    private Restaurant restaurant;
    private Category category;

    @BeforeEach
    public void setup() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        category = new Category();
        category.setId(1L);
        category.setName("Beverages");
        category.setRestaurant(restaurant);
    }

    @Test
    public void testCreateCategory() throws RestaurantException {
        when(restaurantService.getRestaurantsByUserId(anyLong())).thenReturn(restaurant);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory("Beverages", 1L);

        assertNotNull(createdCategory);
        assertEquals("Beverages", createdCategory.getName());
        assertEquals(restaurant, createdCategory.getRestaurant());

        verify(restaurantService, times(1)).getRestaurantsByUserId(anyLong());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testFindCategoryByRestaurantId() throws RestaurantException {
        when(restaurantService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(categoryRepository.findByRestaurantId(anyLong())).thenReturn(Arrays.asList(category));

        List<Category> categories = categoryService.findCategoryByRestaurantId(1L);

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category, categories.get(0));

        verify(restaurantService, times(1)).findRestaurantById(anyLong());
        verify(categoryRepository, times(1)).findByRestaurantId(anyLong());
    }

    @Test
    public void testFindCategoryById() throws RestaurantException {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals(category, foundCategory);

        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFindCategoryByIdThrowsException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        RestaurantException exception = assertThrows(RestaurantException.class, () -> {
            categoryService.findCategoryById(1L);
        });

        assertEquals("category not exist with id 1", exception.getMessage());
        verify(categoryRepository, times(1)).findById(anyLong());
    }
}
