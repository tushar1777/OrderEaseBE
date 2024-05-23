package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zosh.Exception.FoodException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.IngredientsItem;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.repository.IngredientsCategoryRepository;
import com.zosh.repository.foodRepository;
import com.zosh.request.CreateFoodRequest;

@ExtendWith(MockitoExtension.class)
public class FoodServiceImplementationTest {

    @InjectMocks
    private FoodServiceImplementation foodService;

    @Mock
    private foodRepository foodRepository;

    @Mock
    private IngredientsService ingredientService;

    @Mock
    private IngredientsCategoryRepository ingredientCategoryRepo;

    private Food food;
    private Restaurant restaurant;
    private Category category;
    private CreateFoodRequest createFoodRequest;
    private List<IngredientsItem> ingredients;

    @BeforeEach
    public void setup() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setFoods(new ArrayList<>());

        category = new Category();
        category.setId(1L);
        category.setName("Category 1");

        ingredients = new ArrayList<>();
        IngredientsItem ingredient = new IngredientsItem();
        ingredient.setName("Ingredient 1");
        ingredients.add(ingredient);

        createFoodRequest = new CreateFoodRequest();
        createFoodRequest.setName("Food 1");
        createFoodRequest.setDescription("Description 1");
        createFoodRequest.setImages(Collections.singletonList("image.png"));
        createFoodRequest.setPrice(10L);
        createFoodRequest.setSeasonal(true);
        createFoodRequest.setVegetarian(true);
        createFoodRequest.setIngredients(ingredients);

        food = new Food();
        food.setId(1L);
        food.setName("Food 1");
        food.setFoodCategory(category);
        food.setCreationDate(new Date());
        food.setDescription("Description 1");
        food.setImages(Collections.singletonList("image.png"));
        food.setPrice(10L);
        food.setSeasonal(true);
        food.setVegetarian(true);
        food.setIngredients(ingredients);
        food.setRestaurant(restaurant);
    }

    @Test
    public void testCreateFood() throws FoodException, RestaurantException {
        when(foodRepository.save(any(Food.class))).thenReturn(food);

        Food createdFood = foodService.createFood(createFoodRequest, category, restaurant);

        assertNotNull(createdFood);
        assertEquals("Food 1", createdFood.getName());
        assertEquals(category, createdFood.getFoodCategory());
        assertEquals(restaurant, createdFood.getRestaurant());
        assertEquals(ingredients, createdFood.getIngredients());

        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    public void testDeleteFood() throws FoodException {
        when(foodRepository.findById(anyLong())).thenReturn(Optional.of(food));
        doNothing().when(foodRepository).delete(any(Food.class));

        foodService.deleteFood(1L);

        verify(foodRepository, times(1)).findById(anyLong());
        verify(foodRepository, times(1)).delete(any(Food.class));
    }

    @Test
    public void testDeleteFoodThrowsException() {
        when(foodRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FoodException.class, () -> {
            foodService.deleteFood(1L);
        });

        assertEquals("food with id1not found", exception.getMessage());

        verify(foodRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetRestaurantsFood() throws FoodException {
        when(foodRepository.findByRestaurantId(anyLong())).thenReturn(List.of(food));

        List<Food> foods = foodService.getRestaurantsFood(1L, true, false, true, "Category 1");

        assertNotNull(foods);
        assertEquals(1, foods.size());
        assertEquals(food, foods.get(0));

        verify(foodRepository, times(1)).findByRestaurantId(anyLong());
    }

    @Test
    public void testSearchFood() {
        when(foodRepository.searchByNameOrCategory(anyString())).thenReturn(List.of(food));

        List<Food> foods = foodService.searchFood("keyword");

        assertNotNull(foods);
        assertEquals(1, foods.size());
        assertEquals(food, foods.get(0));

        verify(foodRepository, times(1)).searchByNameOrCategory(anyString());
    }

    @Test
    public void testUpdateAvailabilityStatus() throws FoodException {
        // Set the initial availability to true
        food.setAvailable(true);

        when(foodRepository.findById(anyLong())).thenReturn(Optional.of(food));
        when(foodRepository.save(any(Food.class))).thenReturn(food);

        Food updatedFood = foodService.updateAvailibilityStatus(1L);

        assertNotNull(updatedFood);
        assertFalse(updatedFood.isAvailable()); // Expecting availability to be toggled from true to false

        verify(foodRepository, times(1)).findById(anyLong());
        verify(foodRepository, times(1)).save(any(Food.class));
    }


    @Test
    public void testFindFoodById() throws FoodException {
        when(foodRepository.findById(anyLong())).thenReturn(Optional.of(food));

        Food foundFood = foodService.findFoodById(1L);

        assertNotNull(foundFood);
        assertEquals(food, foundFood);

        verify(foodRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFindFoodByIdThrowsException() {
        when(foodRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(FoodException.class, () -> {
            foodService.findFoodById(1L);
        });

        assertEquals("food with id1not found", exception.getMessage());

        verify(foodRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFilterByVegetarian() {
        List<Food> foods = List.of(food);
        List<Food> filteredFoods = foods.stream()
                .filter(f -> f.isVegetarian())
                .collect(Collectors.toList());

        assertNotNull(filteredFoods);
        assertEquals(1, filteredFoods.size());
        assertEquals(food, filteredFoods.get(0));
    }

    @Test
    public void testFilterByNonveg() {
        food.setVegetarian(false);
        List<Food> foods = List.of(food);
        List<Food> filteredFoods = foods.stream()
                .filter(f -> !f.isVegetarian())
                .collect(Collectors.toList());

        assertNotNull(filteredFoods);
        assertEquals(1, filteredFoods.size());
        assertEquals(food, filteredFoods.get(0));
    }

    @Test
    public void testFilterBySeasonal() {
        List<Food> foods = List.of(food);
        List<Food> filteredFoods = foods.stream()
                .filter(Food::isSeasonal)
                .collect(Collectors.toList());

        assertNotNull(filteredFoods);
        assertEquals(1, filteredFoods.size());
        assertEquals(food, filteredFoods.get(0));
    }

    @Test
    public void testFilterByFoodCategory() {
        List<Food> foods = List.of(food);
        List<Food> filteredFoods = foods.stream()
                .filter(f -> f.getFoodCategory() != null && "Category 1".equals(f.getFoodCategory().getName()))
                .collect(Collectors.toList());

        assertNotNull(filteredFoods);
        assertEquals(1, filteredFoods.size());
        assertEquals(food, filteredFoods.get(0));
    }

    @Test
    public void testFilterByFoodCategoryWhenCategoryIsNull() {
        food.setFoodCategory(null);
        List<Food> foods = List.of(food);
        List<Food> filteredFoods = foods.stream()
                .filter(f -> f.getFoodCategory() != null && "Category 1".equals(f.getFoodCategory().getName()))
                .collect(Collectors.toList());

        assertNotNull(filteredFoods);
        assertEquals(0, filteredFoods.size());
    }

    @Test
    public void testGetRestaurantsFoodFilterByNonveg() throws FoodException {
        Food nonVegFood = new Food();
        nonVegFood.setVegetarian(false); // Set the food item as non-vegetarian

        // Create a list of foods with both vegetarian and non-vegetarian items
        List<Food> foods = new ArrayList<>();
        foods.add(food); // Vegetarian food
        foods.add(nonVegFood); // Non-vegetarian food

        // Mock the foodRepository to return the list of foods
        when(foodRepository.findByRestaurantId(anyLong())).thenReturn(foods);

        // Call the getRestaurantsFood method with isNonveg set to true
        List<Food> filteredFoods = foodService.getRestaurantsFood(1L, false, true, false, "Category 1");

        // Verify that only the non-vegetarian food is returned
        assertNotNull(filteredFoods);
        assertEquals(1, filteredFoods.size());
        assertEquals(nonVegFood, filteredFoods.get(0));

        // Verify that the repository method is called with the correct restaurantId
        verify(foodRepository, times(1)).findByRestaurantId(anyLong());
    }
}
