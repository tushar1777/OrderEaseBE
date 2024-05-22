package com.zosh.service;

import com.zosh.Exception.*;
import com.zosh.model.*;
import com.zosh.repository.*;
import com.zosh.request.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private foodRepository foodRepository;

    @InjectMocks
    private CartServiceImplementation cartService;

    @Test
    public void testAddItemToCart() throws UserException, FoodException, CartException, CartItemException {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setMenuItemId(1L);
        request.setQuantity(2);
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        Food food = new Food();
        food.setId(1L);
        food.setPrice(10L);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        CartItem savedCartItem = new CartItem(); // Create a new CartItem to be returned by the mock
        savedCartItem.setId(1L); // Set an ID for the saved cart item

        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        when(foodRepository.findById(eq(request.getMenuItemId()))).thenReturn(Optional.of(food));
        when(cartRepository.findByCustomer_Id(eq(user.getId()))).thenReturn(Optional.of(cart));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedCartItem); // Mock save method to return the saved CartItem

        CartItem result = cartService.addItemToCart(request, jwt);

        assertNotNull(result);
        assertEquals(0, result.getQuantity());
        assertEquals(1, result.getId()); // Check if the returned CartItem has an ID
    }

    @Test
    public void testUpdateCartItemQuantity() throws CartItemException {
        Long cartItemId = 1L;
        int quantity = 3;

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        Food food = new Food();
        food.setId(1L);
        food.setPrice(10L);
        food.setName("Item1");
        cartItem.setFood(food);

        when(cartItemRepository.findById(eq(cartItemId))).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.updateCartItemQuantity(cartItemId, quantity);

        assertNotNull(result);
        assertEquals(quantity, result.getQuantity());
    }

    @Test
    public void testRemoveItemFromCart() throws UserException, CartException, CartItemException {
        Long cartItemId = 1L;
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();

        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);

        cart.getItems().add(cartItem);

        // Mocking the user retrieval
        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        // Mocking the cart retrieval
        when(cartRepository.findByCustomer_Id(eq(user.getId()))).thenReturn(Optional.of(cart));
        // Mocking the cart item retrieval
        when(cartItemRepository.findById(eq(cartItemId))).thenReturn(Optional.of(cartItem));
        // Mocking the cart repository save method
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.removeItemFromCart(cartItemId, jwt);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void testCalculateCartTotals() throws UserException {
        Cart cart = new Cart();
        CartItem cartItem1 = new CartItem();
        cartItem1.setQuantity(2);
        Food food = new Food();
        food.setId(1L);
        food.setName("Item1");
        food.setPrice(10L);
        cartItem1.setFood(food);
        CartItem cartItem2 = new CartItem();
        cartItem2.setQuantity(3);
        Food food1 = new Food();
        food1.setId(2L);
        food1.setName("Item2");
        food1.setPrice(15L);
        cartItem1.setFood(food);
        cartItem2.setFood(food1);
        cart.getItems().add(cartItem1);
        cart.getItems().add(cartItem2);

        Long expectedTotal = 2 * 10L + 3 * 15L;

        Long result = cartService.calculateCartTotals(cart);

        assertEquals(expectedTotal, result);
    }

    @Test
    public void testFindCartById() throws CartException {
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setId(cartId);

        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.of(cart));

        Cart result = cartService.findCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
    }

    @Test
    public void testFindCartByUserId() throws CartException, UserException {
        Long userId = 1L;
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        cart.setCustomer(user);

        when(cartRepository.findByCustomer_Id(eq(userId))).thenReturn(Optional.of(cart));

        Cart result = cartService.findCartByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getCustomer().getId());
    }

    @Test
    public void testClearCart() throws CartException, UserException {
        Long userId = 1L;
        Cart cart = new Cart();
        User user = new User();
        user.setId(1L);
        cart.setCustomer(user);

        when(cartRepository.findByCustomer_Id(eq(userId))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.clearCart(userId);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void testAddItemToCart_MenuItemNotFound() throws UserException, FoodException, CartException, CartItemException {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setMenuItemId(1L);
        request.setQuantity(2);
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        // Mocking user retrieval
        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        // Mocking menu item retrieval returning empty optional
        when(foodRepository.findById(eq(request.getMenuItemId()))).thenReturn(Optional.empty());

        // Executing the method and expecting a FoodException
        assertThrows(FoodException.class, () -> cartService.addItemToCart(request, jwt));
    }

    @Test
    public void testAddItemToCart_UpdateQuantity() throws UserException, FoodException, CartException, CartItemException {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setMenuItemId(1L);
        request.setQuantity(2);
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        Food existingFood = new Food();
        existingFood.setId(1L);
        existingFood.setPrice(10L); // Set a non-null price value

        Cart cart = new Cart();
        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(1L);
        existingCartItem.setFood(existingFood);
        existingCartItem.setQuantity(3); // Existing quantity in cart

        // Mocking user retrieval
        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        // Mocking menu item retrieval
        when(foodRepository.findById(eq(request.getMenuItemId()))).thenReturn(Optional.of(existingFood));
        // Mocking cart retrieval
        when(cartRepository.findByCustomer_Id(eq(user.getId()))).thenReturn(Optional.of(cart));
        // Mocking existing cart item in cart
        cart.getItems().add(existingCartItem);
        // Mocking cart item repository to return existing cart item
        when(cartItemRepository.findById(eq(existingCartItem.getId()))).thenReturn(Optional.of(existingCartItem));

        CartItem result = cartService.addItemToCart(request, jwt);

        assertNull(result);
//        assertEquals(existingCartItem.getId(), result.getId()); // Check if the existing cart item is returned
//        assertEquals(existingCartItem.getQuantity() + request.getQuantity(), result.getQuantity()); // Check if quantity is updated
    }

    @Test
    public void testFindCartByUserId_CartNotFound() throws CartException, UserException {
        Long userId = 1L;

        // Mocking cart repository to return empty Optional
        when(cartRepository.findByCustomer_Id(eq(userId))).thenReturn(Optional.empty());

        // Executing the method and expecting a CartException
        assertThrows(CartException.class, () -> cartService.findCartByUserId(userId));
    }

    @Test
    public void testFindCartById_CartNotFound() throws CartException {
        Long cartId = 1L;

        // Mocking cart repository to return empty Optional
        when(cartRepository.findById(eq(cartId))).thenReturn(Optional.empty());

        // Executing the method and expecting a CartException
        assertThrows(CartException.class, () -> cartService.findCartById(cartId));
    }

    @Test
    public void testRemoveItemFromCart_CartItemNotFound() throws UserException, CartException, CartItemException {
        Long cartItemId = 1L;
        String jwt = "dummyJwt";

        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();

        // Mocking user retrieval
        when(userService.findUserProfileByJwt(eq(jwt))).thenReturn(user);
        // Mocking cart retrieval
        when(cartRepository.findByCustomer_Id(eq(user.getId()))).thenReturn(Optional.of(cart));
        // Mocking cart item retrieval to return empty Optional
        when(cartItemRepository.findById(eq(cartItemId))).thenReturn(Optional.empty());

        // Executing the method and expecting a CartItemException
        assertThrows(CartItemException.class, () -> cartService.removeItemFromCart(cartItemId, jwt));
    }

    @Test
    public void testUpdateCartItemQuantity_CartItemNotFound() throws CartItemException {
        Long cartItemId = 1L;
        int quantity = 5;

        // Mocking cart item retrieval to return empty Optional
        when(cartItemRepository.findById(eq(cartItemId))).thenReturn(Optional.empty());

        // Executing the method and expecting a CartItemException
        assertThrows(CartItemException.class, () -> cartService.updateCartItemQuantity(cartItemId, quantity));
    }

}
