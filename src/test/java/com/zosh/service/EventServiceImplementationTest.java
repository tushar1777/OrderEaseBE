package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zosh.Exception.RestaurantException;
import com.zosh.model.Events;
import com.zosh.model.Restaurant;
import com.zosh.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplementationTest {

    @InjectMocks
    private EventServiceImplementation eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RestaurantService restaurantService;

    private Events event;
    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        restaurant = new Restaurant();
        restaurant.setId(1L);

        event = new Events();
        event.setId(1L);
        event.setName("Event 1");
        event.setImage("image.png");
        event.setStartedAt(String.valueOf(LocalDateTime.now()));
        event.setEndsAt(String.valueOf(LocalDateTime.now().plusHours(2)));
        event.setLocation("Location 1");
        event.setRestaurant(restaurant);
    }

    @Test
    public void testCreateEvent() throws RestaurantException {
        when(restaurantService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(eventRepository.save(any(Events.class))).thenReturn(event);

        Events createdEvent = eventService.createEvent(event, 1L);

        assertNotNull(createdEvent);
        assertEquals("Event 1", createdEvent.getName());
        assertEquals(restaurant, createdEvent.getRestaurant());

        verify(restaurantService, times(1)).findRestaurantById(anyLong());
        verify(eventRepository, times(1)).save(any(Events.class));
    }

    @Test
    public void testFindAllEvent() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Events> events = eventService.findAllEvent();

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void testFindRestaurantsEvent() {
        when(eventRepository.findEventsByRestaurantId(anyLong())).thenReturn(Arrays.asList(event));

        List<Events> events = eventService.findRestaurantsEvent(1L);

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals(event, events.get(0));

        verify(eventRepository, times(1)).findEventsByRestaurantId(anyLong());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(any(Events.class));

        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).delete(any(Events.class));
    }

    @Test
    public void testDeleteEventThrowsException() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            eventService.deleteEvent(1L);
        });

        assertEquals("event not found withy id 1", exception.getMessage());

        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFindById() throws Exception {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        Events foundEvent = eventService.findById(1L);

        assertNotNull(foundEvent);
        assertEquals(event, foundEvent);

        verify(eventRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testFindByIdThrowsException() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            eventService.findById(1L);
        });

        assertEquals("event not found withy id 1", exception.getMessage());

        verify(eventRepository, times(1)).findById(anyLong());
    }
}
