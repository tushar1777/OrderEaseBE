package com.zosh.service;

import com.zosh.Exception.ReviewException;
import com.zosh.model.Review;
import com.zosh.model.User;
import com.zosh.request.ReviewRequest;

import java.util.List;

public interface ReviewSerive {

    Review submitReview(ReviewRequest review, User user);

    void deleteReview(Long reviewId) throws ReviewException;

    double calculateAverageRating(List<Review> reviews);
}
