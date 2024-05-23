package com.zosh.controller;

import com.zosh.model.IngredientCategory;
import com.zosh.model.IngredientsItem;
import com.zosh.request.CreateIngredientCategoryRequest;
import com.zosh.request.CreateIngredientRequest;
import com.zosh.service.IngredientsService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientsControllerTest {

    @InjectMocks
    private IngredientsController ingredientsController;

    @Mock
    private IngredientsService ingredientsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateIngredientCategory() throws Exception {
        // Arrange
        CreateIngredientCategoryRequest request = new CreateIngredientCategoryRequest();
        request.setName("Vegetables");
        request.setRestaurantId(1L);

        IngredientCategory category = new IngredientCategory();
        category.setName("Vegetables");

        when(ingredientsService.createIngredientsCategory(anyString(), anyLong())).thenReturn(category);

        // Act
        ResponseEntity<IngredientCategory> response = ingredientsController.createIngredientCategory(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());

        verify(ingredientsService, times(1)).createIngredientsCategory(request.getName(), request.getRestaurantId());
    }

    @Test
    public void testCreateIngredient() throws Exception {
        // Arrange
        CreateIngredientRequest request = new CreateIngredientRequest();
        request.setName("Tomato");
        request.setRestaurantId(1L);
        request.setIngredientCategoryId(1L);

        IngredientsItem ingredient = new IngredientsItem();
        ingredient.setName("Tomato");

        when(ingredientsService.createIngredientsItem(anyLong(), anyString(), anyLong())).thenReturn(ingredient);

        // Act
        ResponseEntity<IngredientsItem> response = ingredientsController.createIngredient(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredient, response.getBody());

        verify(ingredientsService, times(1)).createIngredientsItem(request.getRestaurantId(), request.getName(), request.getIngredientCategoryId());
    }

    @Test
    public void testUpdateStoke() throws Exception {
        // Arrange
        Long ingredientId = 1L;

        IngredientsItem ingredient = new IngredientsItem();
        ingredient.setName("Tomato");

        when(ingredientsService.updateStoke(anyLong())).thenReturn(ingredient);

        // Act
        ResponseEntity<IngredientsItem> response = ingredientsController.updateStoke(ingredientId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredient, response.getBody());

        verify(ingredientsService, times(1)).updateStoke(ingredientId);
    }

    @Test
    public void testRestaurantsIngredient() throws Exception {
        // Arrange
        Long restaurantId = 1L;

        IngredientsItem ingredient1 = new IngredientsItem();
        ingredient1.setName("Tomato");

        IngredientsItem ingredient2 = new IngredientsItem();
        ingredient2.setName("Lettuce");

        List<IngredientsItem> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientsService.findRestaurantsIngredients(anyLong())).thenReturn(ingredients);

        // Act
        ResponseEntity<List<IngredientsItem>> response = ingredientsController.restaurantsIngredient(restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingredients, response.getBody());

        verify(ingredientsService, times(1)).findRestaurantsIngredients(restaurantId);
    }

    @Test
    public void testRestaurantsIngredientCategory() throws Exception {
        // Arrange
        Long restaurantId = 1L;

        IngredientCategory category1 = new IngredientCategory();
        category1.setName("Vegetables");

        IngredientCategory category2 = new IngredientCategory();
        category2.setName("Fruits");

        List<IngredientCategory> categories = Arrays.asList(category1, category2);

        when(ingredientsService.findIngredientsCategoryByRestaurantId(anyLong())).thenReturn(categories);

        // Act
        ResponseEntity<List<IngredientCategory>> response = ingredientsController.restaurantsIngredientCategory(restaurantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());

        verify(ingredientsService, times(1)).findIngredientsCategoryByRestaurantId(restaurantId);
    }
}
