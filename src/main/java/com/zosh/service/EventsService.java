package com.zosh.service;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Events;

import java.util.List;

public interface EventsService {

    Events createEvent(Events event, Long restaurantId) throws RestaurantException;

    List<Events> findAllEvent();

    List<Events> findRestaurantsEvent(Long id);

    void deleteEvent(Long id) throws Exception;

    Events findById(Long id) throws Exception;

}
