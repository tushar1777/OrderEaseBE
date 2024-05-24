package com.zosh.service;

import com.zosh.Exception.RestaurantException;
import com.zosh.dto.RestaurantDto;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    Restaurant createRestaurant(CreateRestaurantRequest req, User user);

    Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant)
            throws RestaurantException;

    void deleteRestaurant(Long restaurantId) throws RestaurantException;

    List<Restaurant> getAllRestaurant();

    List<Restaurant> searchRestaurant(String keyword);

    Restaurant findRestaurantById(Long id) throws RestaurantException;

    Restaurant getRestaurantsByUserId(Long userId) throws RestaurantException;

    RestaurantDto addToFavorites(Long restaurantId, User user) throws RestaurantException;

    Restaurant updateRestaurantStatus(Long id) throws RestaurantException;
}
