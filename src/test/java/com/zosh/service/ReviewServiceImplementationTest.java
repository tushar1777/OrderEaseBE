package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.ReviewException;
import com.zosh.model.Restaurant;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.repository.RestaurantRepository;
import com.zosh.repository.ReviewRepository;
import com.zosh.request.ReviewRequest;

public class ReviewServiceImplementationTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitReview() {
        // Arrange
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setReviewText("Great restaurant!");
        reviewRequest.setRating(5);
        reviewRequest.setRestaurantId(1L);

        User user = new User();
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Review result = reviewService.submitReview(reviewRequest, user);

        // Assert
        assertNotNull(result);
        assertEquals("Great restaurant!", result.getMessage());
        assertEquals(5, result.getRating());
        assertEquals(user, result.getCustomer());
        assertEquals(restaurant, result.getRestaurant());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void testDeleteReview() {
        // Arrange
        Long reviewId = 1L;
        Review review = new Review();
        review.setId(reviewId);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        assertDoesNotThrow(() -> reviewService.deleteReview(reviewId));

        // Assert
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testCalculateAverageRating() {
        // Arrange
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1L, 5, "Great!", LocalDateTime.now()));
        reviews.add(new Review(2L, 3, "Average", LocalDateTime.now()));

        // Act
        double averageRating = reviewService.calculateAverageRating(reviews);

        // Assert
        assertEquals(4, averageRating);
    }
}


