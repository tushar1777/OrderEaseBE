package com.zosh.repository;

import com.zosh.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Events, Long> {

    List<Events> findEventsByRestaurantId(Long id);
}
