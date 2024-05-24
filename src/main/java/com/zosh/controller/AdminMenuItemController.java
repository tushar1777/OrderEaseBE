package com.zosh.controller;

import com.zosh.Exception.FoodException;
import com.zosh.Exception.RestaurantException;
import com.zosh.Exception.UserException;
import com.zosh.model.Food;
import com.zosh.model.Restaurant;
import com.zosh.model.User;
import com.zosh.request.CreateFoodRequest;
import com.zosh.service.CategoryService;
import com.zosh.service.FoodService;
import com.zosh.service.RestaurantService;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/food")
public class AdminMenuItemController {

    @Autowired
    private FoodService menuItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Food> createItem(
            @RequestBody CreateFoodRequest item,
            @RequestHeader("Authorization") String jwt)
            throws FoodException, UserException, RestaurantException {
        System.out.println("req-controller ----" + item);
        User user = userService.findUserProfileByJwt(jwt);
//		Category category=categoryService.findCategoryById(item.getCategoryId());
        Restaurant restaurant = restaurantService.findRestaurantById(item.getRestaurantId());
        Food menuItem = menuItemService.createFood(item, item.getCategory(), restaurant);
        return ResponseEntity.ok(menuItem);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id, @RequestHeader("Authorization") String jwt)
            throws UserException, FoodException {
        User user = userService.findUserProfileByJwt(jwt);

        menuItemService.deleteFood(id);
        return ResponseEntity.ok("Menu item deleted");


    }


    @GetMapping("/search")
    public ResponseEntity<List<Food>> getMenuItemByName(@RequestParam String name) {
        List<Food> menuItem = menuItemService.searchFood(name);
        return ResponseEntity.ok(menuItem);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Food> updateAvilibilityStatus(
            @PathVariable Long id) throws FoodException {
        Food menuItems = menuItemService.updateAvailibilityStatus(id);
        return ResponseEntity.ok(menuItems);
    }


}
