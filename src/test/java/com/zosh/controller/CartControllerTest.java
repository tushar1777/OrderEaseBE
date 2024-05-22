package com.zosh.controller;

import com.zosh.Exception.*;
import com.zosh.model.*;
import com.zosh.request.*;
import com.zosh.service.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Mock
    private CartServiceImplementation cartService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartController cartController;

    @Test
    public void testAddItemToCart() throws UserException, FoodException, CartException, CartItemException {
        AddCartItemRequest request = new AddCartItemRequest();
        String jwt = "dummyJwt";

        when(cartService.addItemToCart(any(AddCartItemRequest.class), eq(jwt))).thenReturn(new CartItem());

        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);

        Assertions.assertNotNull(response);
    }

    @Test
    public void testUpdateCartItemQuantity() throws CartItemException {

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setCartItemId(1L);
        String jwt = "dummyJwt";

        when(cartService.updateCartItemQuantity(anyLong(), anyInt())).thenReturn(new CartItem());

        ResponseEntity<CartItem> response = cartController.updateCartItemQuantity(request, jwt);

        Assertions.assertNotNull(response);
    }


    @Test
    public void testRemoveItemFromCart() throws UserException, CartException, CartItemException {
        Long itemId = 1L;
        String jwt = "dummyJwt";

        when(cartService.removeItemFromCart(eq(itemId), eq(jwt))).thenReturn(new Cart());

        ResponseEntity<Cart> response = cartController.removeItemFromCart(itemId, jwt);

        Assertions.assertNotNull(response);
    }

    @Test
    public void testCalculateCartTotals() throws UserException, CartException {
        Long cartId = 1L;
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(cartId); // Set user ID to match cart ID for testing purposes

        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        when(cartService.findCartByUserId(eq(cartId))).thenReturn(new Cart());
        when(cartService.calculateCartTotals(any(Cart.class))).thenReturn(0L);

        ResponseEntity<Double> response = cartController.calculateCartTotals(cartId, jwt);

        Assertions.assertNotNull(response);
    }


    @Test
    public void testFindUserCart() throws UserException, CartException {
        String jwt = "dummyJwt";

        Cart cart = new Cart();
        cart.setId(1L);

        User user = new User();
        user.setId(0L);

        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        when(cartService.findCartByUserId(anyLong())).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.findUserCart(jwt);

        Assertions.assertNotNull(response);
    }

    @Test
    public void testClearCart() throws UserException, CartException {
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        when(cartService.clearCart(anyLong())).thenReturn(new Cart());

        ResponseEntity<Cart> response = cartController.cleareCart(jwt);

        Assertions.assertNotNull(response);
    }
}
