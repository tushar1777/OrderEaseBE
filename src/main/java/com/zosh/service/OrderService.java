package com.zosh.service;

import com.stripe.exception.StripeException;
import com.zosh.Exception.CartException;
import com.zosh.Exception.OrderException;
import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.model.Order;
import com.zosh.model.PaymentResponse;
import com.zosh.model.User;
import com.zosh.request.CreateOrderRequest;

import java.util.List;

public interface OrderService {

    PaymentResponse createOrder(CreateOrderRequest order, User user) throws UserException, RestaurantException, CartException, StripeException;

    Order updateOrder(Long orderId, String orderStatus) throws OrderException;

    void cancelOrder(Long orderId) throws OrderException;

    List<Order> getUserOrders(Long userId) throws OrderException;

    List<Order> getOrdersOfRestaurant(Long restaurantId, String orderStatus) throws OrderException, RestaurantException;


}
