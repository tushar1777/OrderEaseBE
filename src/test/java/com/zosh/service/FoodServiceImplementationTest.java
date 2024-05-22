package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.zosh.repository.IngredientsCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.FoodException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.repository.foodRepository;
import com.zosh.request.CreateFoodRequest;

public class FoodServiceImplementationTest {

    @Mock
    private foodRepository foodRepository;

    @Mock
    private IngredientsService ingredientService;

    @Mock
    private IngredientsCategoryRepository ingredientCategoryRepo;

    @InjectMocks
    private FoodServiceImplementation foodService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFood() throws FoodException, RestaurantException {
        // Arrange
        CreateFoodRequest req = new CreateFoodRequest();
        req.setDescription("Delicious food");
        req.setImages(new ArrayList<>());
        req.setName("Food Name");
        req.setPrice(100L);
        req.setSeasonal(true);
        req.setVegetarian(true);
        req.setIngredients(new ArrayList<>());

        Category category = new Category();
        Restaurant restaurant = new Restaurant();

        Food food = new Food();
        food.setFoodCategory(category);
        food.setCreationDate(new Date());
        food.setDescription(req.getDescription());
        food.setImages(req.getImages());
        food.setName(req.getName());
        food.setPrice((long) req.getPrice());
        food.setSeasonal(req.isSeasonal());
        food.setVegetarian(req.isVegetarian());
        food.setIngredients(req.getIngredients());
        food.setRestaurant(restaurant);

        when(foodRepository.save(any(Food.class))).thenReturn(food);

        // Act
        Food result = foodService.createFood(req, category, restaurant);

        // Assert
        assertNotNull(result);
        assertEquals(req.getDescription(), result.getDescription());
        assertEquals(req.getName(), result.getName());
        assertEquals(req.getPrice(), result.getPrice());
        assertEquals(req.isSeasonal(), result.isSeasonal());
        assertEquals(req.isVegetarian(), result.isVegetarian());

        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    void testDeleteFood() throws FoodException {
        // Arrange
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

        // Act
        foodService.deleteFood(foodId);

        // Assert
        verify(foodRepository, times(1)).findById(foodId);
        verify(foodRepository, times(1)).delete(food);
    }

    @Test
    void testDeleteFood_FoodNotFound() {
        // Arrange
        Long foodId = 1L;

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FoodException.class, () -> foodService.deleteFood(foodId));

        verify(foodRepository, times(1)).findById(foodId);
        verify(foodRepository, never()).delete(any(Food.class));
    }

    @Test
    void testGetRestaurantsFood() throws FoodException {
        // Arrange
        Long restaurantId = 1L;
        Food food1 = new Food();
        food1.setVegetarian(true);
        food1.setSeasonal(true);

        Food food2 = new Food();
        food2.setVegetarian(false);
        food2.setSeasonal(false);

        List<Food> foods = new ArrayList<>();
        foods.add(food1);
        foods.add(food2);

        when(foodRepository.findByRestaurantId(restaurantId)).thenReturn(foods);

        // Act
        List<Food> result = foodService.getRestaurantsFood(restaurantId, true, false, true, "");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isVegetarian());

        verify(foodRepository, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testSearchFood() {
        // Arrange
        String keyword = "Pizza";
        List<Food> foods = new ArrayList<>();
        Food food = new Food();
        food.setName("Pizza");
        foods.add(food);

        when(foodRepository.searchByNameOrCategory(keyword)).thenReturn(foods);

        // Act
        List<Food> result = foodService.searchFood(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());

        verify(foodRepository, times(1)).searchByNameOrCategory(keyword);
    }

    @Test
    void testUpdateAvailibilityStatus() throws FoodException {
        // Arrange
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);
        food.setAvailable(true);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(foodRepository.save(any(Food.class))).thenReturn(food);

        // Act
        Food result = foodService.updateAvailibilityStatus(foodId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isAvailable());

        verify(foodRepository, times(1)).findById(foodId);
        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    void testFindFoodById() throws FoodException {
        // Arrange
        Long foodId = 1L;
        Food food = new Food();
        food.setId(foodId);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));

        // Act
        Food result = foodService.findFoodById(foodId);

        // Assert
        assertNotNull(result);
        assertEquals(foodId, result.getId());

        verify(foodRepository, times(1)).findById(foodId);
    }

    @Test
    void testFindFoodById_FoodNotFound() {
        // Arrange
        Long foodId = 1L;

        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FoodException.class, () -> foodService.findFoodById(foodId));

        verify(foodRepository, times(1)).findById(foodId);
    }

    // Additional tests for other methods and edge cases can be added similarly
}

