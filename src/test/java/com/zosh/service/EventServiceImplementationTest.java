package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Events;
import com.zosh.model.Restaurant;
import com.zosh.repository.EventRepository;
import com.zosh.service.EventServiceImplementation;
import com.zosh.service.RestaurantService;

public class EventServiceImplementationTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private EventServiceImplementation eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() throws RestaurantException {
        // Arrange
        Long restaurantId = 1L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Events event = new Events();
        event.setImage("image.png");
        event.setStartedAt(String.valueOf(LocalDateTime.now()));
        event.setEndsAt(String.valueOf(LocalDateTime.now().plusHours(2)));
        event.setLocation("Location");
        event.setName("Event Name");

        when(restaurantService.findRestaurantById(restaurantId)).thenReturn(restaurant);
        when(eventRepository.save(any(Events.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Events result = eventService.createEvent(event, restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(restaurant, result.getRestaurant());
        assertEquals(event.getImage(), result.getImage());
        assertEquals(event.getStartedAt(), result.getStartedAt());
        assertEquals(event.getEndsAt(), result.getEndsAt());
        assertEquals(event.getLocation(), result.getLocation());
        assertEquals(event.getName(), result.getName());

        verify(restaurantService, times(1)).findRestaurantById(restaurantId);
        verify(eventRepository, times(1)).save(any(Events.class));
    }

    @Test
    void testFindAllEvent() {
        // Arrange
        Events event1 = new Events();
        event1.setName("Event 1");

        Events event2 = new Events();
        event2.setName("Event 2");

        List<Events> events = Arrays.asList(event1, event2);

        when(eventRepository.findAll()).thenReturn(events);

        // Act
        List<Events> result = eventService.findAllEvent();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("Event 2", result.get(1).getName());

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testFindRestaurantsEvent() {
        // Arrange
        Long restaurantId = 1L;

        Events event1 = new Events();
        event1.setName("Event 1");

        Events event2 = new Events();
        event2.setName("Event 2");

        List<Events> events = Arrays.asList(event1, event2);

        when(eventRepository.findEventsByRestaurantId(restaurantId)).thenReturn(events);

        // Act
        List<Events> result = eventService.findRestaurantsEvent(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("Event 2", result.get(1).getName());

        verify(eventRepository, times(1)).findEventsByRestaurantId(restaurantId);
    }

    @Test
    void testDeleteEvent() throws Exception {
        // Arrange
        Long eventId = 1L;

        Events event = new Events();
        event.setId(eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);

        // Act
        eventService.deleteEvent(eventId);

        // Assert
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).delete(event);
    }

    @Test
    void testDeleteEvent_EventNotFound() {
        // Arrange
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            eventService.deleteEvent(eventId);
        });

        assertEquals("event not found withy id " + eventId, exception.getMessage());

        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, never()).delete(any(Events.class));
    }

    @Test
    void testFindById() throws Exception {
        // Arrange
        Long eventId = 1L;

        Events event = new Events();
        event.setId(eventId);
        event.setName("Event Name");

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        Events result = eventService.findById(eventId);

        // Assert
        assertNotNull(result);
        assertEquals(eventId, result.getId());
        assertEquals("Event Name", result.getName());

        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void testFindById_EventNotFound() {
        // Arrange
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            eventService.findById(eventId);
        });

        assertEquals("event not found withy id " + eventId, exception.getMessage());

        verify(eventRepository, times(1)).findById(eventId);
    }

    // Additional tests for other edge cases can be added similarly
}

