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
import com.zosh.model.IngredientCategory;
import com.zosh.model.IngredientsItem;
import com.zosh.model.Restaurant;
import com.zosh.repository.IngredientsCategoryRepository;
import com.zosh.repository.IngredientsItemRepository;

public class IngredientsServiceImplementationTest {

    @Mock
    private IngredientsCategoryRepository ingredientsCategoryRepo;

    @Mock
    private IngredientsItemRepository ingredientsItemRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private IngredientsServiceImplementation ingredientsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIngredientsCategory() throws RestaurantException {
        // Arrange
        String categoryName = "Vegetables";
        Long restaurantId = 1L;
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setName(categoryName);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(ingredientsCategoryRepo.findByRestaurantIdAndNameIgnoreCase(restaurantId, categoryName)).thenReturn(null);
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(ingredientsCategoryRepo.save(any(IngredientCategory.class))).thenReturn(ingredientCategory);

        // Act
        IngredientCategory result = ingredientsService.createIngredientsCategory(categoryName, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryName, result.getName());

        verify(ingredientsCategoryRepo, times(1)).findByRestaurantIdAndNameIgnoreCase(restaurantId, categoryName);
        verify(restaurantService, times(1)).findRestaurantById(restaurantId);
        verify(ingredientsCategoryRepo, times(1)).save(any(IngredientCategory.class));
    }

    @Test
    void testFindIngredientsCategoryById() throws Exception {
        // Arrange
        Long categoryId = 1L;
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setId(categoryId);

        when(ingredientsCategoryRepo.findById(categoryId)).thenReturn(Optional.of(ingredientCategory));

        // Act
        IngredientCategory result = ingredientsService.findIngredientsCategoryById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getId());

        verify(ingredientsCategoryRepo, times(1)).findById(categoryId);
    }

    @Test
    void testFindIngredientsCategoryById_NotFound() {
        // Arrange
        Long categoryId = 1L;

        when(ingredientsCategoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> ingredientsService.findIngredientsCategoryById(categoryId));

        verify(ingredientsCategoryRepo, times(1)).findById(categoryId);
    }

    @Test
    void testFindIngredientsCategoryByRestaurantId() throws Exception {
        // Arrange
        Long restaurantId = 1L;
        List<IngredientCategory> categories = new ArrayList<>();
        categories.add(new IngredientCategory());

        when(ingredientsCategoryRepo.findByRestaurantId(restaurantId)).thenReturn(categories);

        // Act
        List<IngredientCategory> result = ingredientsService.findIngredientsCategoryByRestaurantId(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(ingredientsCategoryRepo, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testFindRestaurantsIngredients() {
        // Arrange
        Long restaurantId = 1L;
        List<IngredientsItem> ingredients = new ArrayList<>();
        ingredients.add(new IngredientsItem());

        when(ingredientsItemRepository.findByRestaurantId(restaurantId)).thenReturn(ingredients);

        // Act
        List<IngredientsItem> result = ingredientsService.findRestaurantsIngredients(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        verify(ingredientsItemRepository, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    public void testIngredientItemExists() throws Exception {
        Long restaurantId = 1L;
        String ingredientName = "Tomato";
        Long ingredientCategoryId = 1L;

        IngredientCategory category = new IngredientCategory();
        category.setId(ingredientCategoryId);
        category.setName("Vegetables");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        IngredientsItem ingredientItem = new IngredientsItem();
        ingredientItem.setName(ingredientName);
        ingredientItem.setCategory(category);
        ingredientItem.setRestaurant(restaurant);

        when(ingredientsItemRepository.findByRestaurantIdAndNameIngoreCase(restaurantId, ingredientName, category.getName())).thenReturn(ingredientItem);
        when(ingredientsCategoryRepo.findById(any())).thenReturn(Optional.of(category));
        when(ingredientsCategoryRepo.findByRestaurantIdAndNameIgnoreCase(restaurantId,category.getName())).thenReturn(category);

        IngredientsItem result = ingredientsService.createIngredientsItem(restaurantId, ingredientName, ingredientCategoryId);
        IngredientCategory result1 = ingredientsService.createIngredientsCategory(category.getName(), restaurantId);

        assertEquals(ingredientName, result.getName());
        assertEquals(category.getName(), result1.getName());
    }

    @Test
    void testCreateIngredientsItem() throws Exception {
        // Arrange
        Long restaurantId = 1L;
        String ingredientName = "Tomato";
        Long ingredientCategoryId = 1L;

        IngredientCategory category = new IngredientCategory();
        category.setId(ingredientCategoryId);
        category.setName("Vegetables");

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        IngredientsItem ingredientItem = new IngredientsItem();
        ingredientItem.setName(ingredientName);
        ingredientItem.setCategory(category);
        ingredientItem.setRestaurant(restaurant);

        when(ingredientsCategoryRepo.findById(ingredientCategoryId)).thenReturn(Optional.of(category));
        when(ingredientsItemRepository.findByRestaurantIdAndNameIngoreCase(restaurantId, ingredientName, category.getName())).thenReturn(null);
        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(ingredientsItemRepository.save(any(IngredientsItem.class))).thenReturn(ingredientItem);

        // Act
        IngredientsItem result = ingredientsService.createIngredientsItem(restaurantId, ingredientName, ingredientCategoryId);

        // Assert
        assertNotNull(result);
        assertEquals(ingredientName, result.getName());

        verify(ingredientsCategoryRepo, times(1)).findById(ingredientCategoryId);
        verify(ingredientsItemRepository, times(1)).findByRestaurantIdAndNameIngoreCase(restaurantId, ingredientName, category.getName());
        verify(restaurantService, times(1)).findRestaurantById(restaurantId);
        verify(ingredientsItemRepository, times(1)).save(any(IngredientsItem.class));
    }

    @Test
    void testUpdateStoke() throws Exception {
        // Arrange
        Long itemId = 1L;
        IngredientsItem ingredientItem = new IngredientsItem();
        ingredientItem.setId(itemId);
        ingredientItem.setInStoke(false);

        when(ingredientsItemRepository.findById(itemId)).thenReturn(Optional.of(ingredientItem));
        when(ingredientsItemRepository.save(any(IngredientsItem.class))).thenReturn(ingredientItem);

        // Act
        IngredientsItem result = ingredientsService.updateStoke(itemId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isInStoke());

        verify(ingredientsItemRepository, times(1)).findById(itemId);
        verify(ingredientsItemRepository, times(1)).save(any(IngredientsItem.class));
    }

    @Test
    void testUpdateStoke_ItemNotFound() {
        // Arrange
        Long itemId = 1L;

        when(ingredientsItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> ingredientsService.updateStoke(itemId));

        verify(ingredientsItemRepository, times(1)).findById(itemId);
    }

    // Additional tests for other methods and edge cases can be added similarly
}

