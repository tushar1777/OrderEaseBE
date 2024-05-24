package com.zosh.service;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.IngredientCategory;
import com.zosh.model.IngredientsItem;

import java.util.List;

public interface IngredientsService {

    IngredientCategory createIngredientsCategory(
            String name, Long restaurantId) throws RestaurantException;

    IngredientCategory findIngredientsCategoryById(Long id) throws Exception;

    List<IngredientCategory> findIngredientsCategoryByRestaurantId(Long id) throws Exception;

    List<IngredientsItem> findRestaurantsIngredients(
            Long restaurantId);


    IngredientsItem createIngredientsItem(Long restaurantId,
                                          String ingredientName, Long ingredientCategoryId) throws Exception;

    IngredientsItem updateStoke(Long id) throws Exception;

}
