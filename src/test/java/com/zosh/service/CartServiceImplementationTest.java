package com.zosh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zosh.Exception.CartException;
import com.zosh.Exception.CartItemException;
import com.zosh.Exception.FoodException;
import com.zosh.Exception.UserException;
import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.model.Food;
import com.zosh.model.User;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.repository.foodRepository;
import com.zosh.request.AddCartItemRequest;
import com.zosh.service.CartServiceImplementation;
import com.zosh.service.UserService;

public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private foodRepository menuItemRepository;

    @InjectMocks
    private CartServiceImplementation cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItemToCart_NewItem() throws UserException, FoodException, CartException, CartItemException {
        // Arrange
        String jwt = "valid-jwt";
        AddCartItemRequest request = new AddCartItemRequest();
        request.setMenuItemId(1L);
        request.setQuantity(2);
        request.setIngredients(Collections.singletonList("Cheese"));

        User user = new User();
        user.setId(1L);

        Food food = new Food();
        food.setId(1L);
        food.setPrice((long) 10.0);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(menuItemRepository.findById(request.getMenuItemId())).thenReturn(Optional.of(food));
        when(cartRepository.findByCustomer_Id(user.getId())).thenReturn(Optional.of(cart));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArguments()[0]);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        CartItem result = cartService.addItemToCart(request, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(request.getQuantity(), result.getQuantity());
        assertEquals(food, result.getFood());
        assertEquals(cart, result.getCart());
        assertEquals(request.getIngredients(), result.getIngredients());
        assertEquals(request.getQuantity() * food.getPrice(), result.getTotalPrice());

        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(menuItemRepository, times(1)).findById(request.getMenuItemId());
        verify(cartRepository, times(1)).findByCustomer_Id(user.getId());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    // Additional tests for other methods and edge cases can be added similarly

}

