package com.zosh.controller;

import com.stripe.exception.StripeException;
import com.zosh.Exception.CartException;
import com.zosh.Exception.OrderException;
import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.model.Order;
import com.zosh.model.PaymentResponse;
import com.zosh.model.User;
import com.zosh.request.CreateOrderRequest;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody CreateOrderRequest order,
                                                       @RequestHeader("Authorization") String jwt)
            throws UserException, RestaurantException,
            CartException,
            StripeException,
            OrderException {
        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("req user " + user.getEmail());
        if (order != null) {
            PaymentResponse res = orderService.createOrder(order, user);
            return ResponseEntity.ok(res);

        } else throw new OrderException("Please provide valid request body");

    }


    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getAllUserOrders(@RequestHeader("Authorization") String jwt) throws OrderException, UserException {

        User user = userService.findUserProfileByJwt(jwt);

        if (user.getId() != null) {
            List<Order> userOrders = orderService.getUserOrders(user.getId());
            return ResponseEntity.ok(userOrders);
        } else {
            return new ResponseEntity<List<Order>>(HttpStatus.BAD_REQUEST);
        }
    }


}
