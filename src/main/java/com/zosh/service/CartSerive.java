package com.zosh.service;

import com.zosh.Exception.CartException;
import com.zosh.Exception.CartItemException;
import com.zosh.Exception.FoodException;
import com.zosh.Exception.UserException;
import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.request.AddCartItemRequest;

public interface CartSerive {

    CartItem addItemToCart(AddCartItemRequest req, String jwt) throws UserException, FoodException, CartException, CartItemException;

    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws CartItemException;

    Cart removeItemFromCart(Long cartItemId, String jwt) throws UserException, CartException, CartItemException;

    Long calculateCartTotals(Cart cart) throws UserException;

    Cart findCartById(Long id) throws CartException;

    Cart findCartByUserId(Long userId) throws CartException, UserException;

    Cart clearCart(Long userId) throws CartException, UserException;


}
