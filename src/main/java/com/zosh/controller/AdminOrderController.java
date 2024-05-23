package com.zosh.controller;

import com.zosh.Exception.OrderException;
import com.zosh.Exception.RestaurantException;
import com.zosh.model.Order;
import com.zosh.service.OrderService;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) throws OrderException {
        if (orderId != null) {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order deleted with id)" + orderId);
        } else return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/order/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getAllRestaurantOrders(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String order_status) throws OrderException, RestaurantException {

        List<Order> orders = orderService.
                getOrdersOfRestaurant(restaurantId, order_status);

//    		System.out.println("ORDER STATUS----- "+orderStatus);
        return ResponseEntity.ok(orders);


    }

    @PutMapping("/orders/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrders(@PathVariable Long orderId, @PathVariable String orderStatus) throws OrderException, RestaurantException {

        Order orders = orderService.updateOrder(orderId, orderStatus);
        return ResponseEntity.ok(orders);

    }

}
