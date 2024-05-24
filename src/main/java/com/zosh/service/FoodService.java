package com.zosh.service;

import com.zosh.Exception.FoodException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    Food createFood(CreateFoodRequest req, Category category,
                    Restaurant restaurant) throws FoodException, RestaurantException;

    void deleteFood(Long foodId) throws FoodException;

    List<Food> getRestaurantsFood(Long restaurantId,
                                  boolean isVegetarian, boolean isNonveg, boolean isSeasonal, String foodCategory) throws FoodException;

    List<Food> searchFood(String keyword);

    Food findFoodById(Long foodId) throws FoodException;

    Food updateAvailibilityStatus(Long foodId) throws FoodException;
}
