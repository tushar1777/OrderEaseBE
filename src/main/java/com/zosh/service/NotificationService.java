package com.zosh.service;

import com.zosh.model.Notification;
import com.zosh.model.Order;
import com.zosh.model.Restaurant;
import com.zosh.model.User;

import java.util.List;

public interface NotificationService {

    Notification sendOrderStatusNotification(Order order);

    void sendRestaurantNotification(Restaurant restaurant, String message);

    void sendPromotionalNotification(User user, String message);

    List<Notification> findUsersNotification(Long userId);

}
