package com.zosh.controller;

import com.zosh.Exception.CartException;
import com.zosh.Exception.CartItemException;
import com.zosh.Exception.FoodException;
import com.zosh.Exception.UserException;
import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.model.User;
import com.zosh.request.AddCartItemRequest;
import com.zosh.request.UpdateCartItemRequest;
import com.zosh.service.CartSerive;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartSerive cartService;
    @Autowired
    private UserService userService;

    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws UserException, FoodException, CartException, CartItemException {
        CartItem cart = cartService.addItemToCart(req, jwt);
        return ResponseEntity.ok(cart);

    }

    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest req,
            @RequestHeader("Authorization") String jwt) throws CartItemException {
        CartItem cart = cartService.updateCartItemQuantity(req.getCartItemId(), req.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart-item/{id}/remove")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, CartException, CartItemException {

        Cart cart = cartService.removeItemFromCart(id, jwt);
        return ResponseEntity.ok(cart);

    }

    @GetMapping("/cart/total")
    public ResponseEntity<Double> calculateCartTotals(@RequestParam Long cartId,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, CartException {


        User user = userService.findUserProfileByJwt(jwt);

        Cart cart = cartService.findCartByUserId(user.getId());
        double total = cartService.calculateCartTotals(cart);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/cart/")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String jwt) throws UserException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findCartByUserId(user.getId());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> cleareCart(
            @RequestHeader("Authorization") String jwt) throws UserException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.clearCart(user.getId());
        return ResponseEntity.ok(cart);
    }

}
