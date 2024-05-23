package com.zosh.repository;

import com.zosh.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByCustomerId(Long userId);

    List<Notification> findByRestaurantId(Long restaurantId);

}
