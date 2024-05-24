package com.zosh.service;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name, Long userId) throws RestaurantException;

    List<Category> findCategoryByRestaurantId(Long restaurantId) throws RestaurantException;

    Category findCategoryById(Long id) throws RestaurantException;

}
