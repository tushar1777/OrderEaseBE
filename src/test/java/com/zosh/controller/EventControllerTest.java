package com.zosh.controller;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Events;
import com.zosh.response.ApiResponse;
import com.zosh.service.EventsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventsService eventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvents() throws RestaurantException {
        // Arrange
        Events event = new Events();
        event.setName("New Year Party");
        Long restaurantId = 1L;

        Events createdEvent = new Events();
        createdEvent.setName("New Year Party");

        when(eventService.createEvent(any(Events.class), anyLong())).thenReturn(createdEvent);

        // Act
        ResponseEntity<Events> response = eventController.createEvents(event, restaurantId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(createdEvent, response.getBody());

        verify(eventService, times(1)).createEvent(event, restaurantId);
    }

    @Test
    public void testFindAllEvents() throws RestaurantException {
        // Arrange
        Events event1 = new Events();
        event1.setName("New Year Party");

        Events event2 = new Events();
        event2.setName("Christmas Party");

        List<Events> events = Arrays.asList(event1, event2);

        when(eventService.findAllEvent()).thenReturn(events);

        // Act
        ResponseEntity<List<Events>> response = eventController.findAllEvents();

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(events, response.getBody());

        verify(eventService, times(1)).findAllEvent();
    }

    @Test
    public void testFindRestaurantsEvents() throws RestaurantException {
        // Arrange
        Long restaurantId = 1L;

        Events event1 = new Events();
        event1.setName("New Year Party");

        Events event2 = new Events();
        event2.setName("Christmas Party");

        List<Events> events = Arrays.asList(event1, event2);

        when(eventService.findRestaurantsEvent(anyLong())).thenReturn(events);

        // Act
        ResponseEntity<List<Events>> response = eventController.findRestaurantsEvents(restaurantId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(events, response.getBody());

        verify(eventService, times(1)).findRestaurantsEvent(restaurantId);
    }

    @Test
    public void testDeleteEvents() throws Exception {
        // Arrange
        Long eventId = 1L;

        // Act
        ResponseEntity<ApiResponse> response = eventController.deleteEvents(eventId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Events Deleted", response.getBody().getMessage());
        assertEquals(true, response.getBody().isStatus());

        verify(eventService, times(1)).deleteEvent(eventId);
    }
}
