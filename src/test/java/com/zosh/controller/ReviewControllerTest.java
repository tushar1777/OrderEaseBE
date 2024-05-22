package com.zosh.controller;

import com.zosh.Exception.ReviewException;
import com.zosh.Exception.UserException;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.request.ReviewRequest;
import com.zosh.service.ReviewSerive;
import com.zosh.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewSerive reviewSerive;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReview() throws UserException {
        // Given
        ReviewRequest reviewRequest = new ReviewRequest();
        User user = new User();
        when(userService.findUserProfileByJwt(any(String.class))).thenReturn(user);
        when(reviewSerive.submitReview(any(ReviewRequest.class), any(User.class))).thenReturn(new Review(1L, 5, "Test Review", LocalDateTime.now()));

        // When
        ResponseEntity<Review> response = reviewController.createReview(reviewRequest, "dummy_jwt");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteReview() throws ReviewException {
        // Given
        Long reviewId = 1L;
        doNothing().when(reviewSerive).deleteReview(reviewId);

        // When
        ResponseEntity<String> response = reviewController.deleteReview(reviewId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Review deleted successfully", response.getBody());
    }

    @Test
    public void testCalculateAverageRating() {
        // Given
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1L, 5, "Test Review 1", LocalDateTime.now()));
        reviews.add(new Review(2L, 4, "Test Review 2", LocalDateTime.now()));
        double expectedAverage = 4.5;
        when(reviewSerive.calculateAverageRating(reviews)).thenReturn(expectedAverage);

        // When
        ResponseEntity<Double> response = reviewController.calculateAverageRating(reviews);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAverage, response.getBody());
    }
}
