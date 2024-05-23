package com.zosh.service;

import com.zosh.Exception.UserException;
import com.zosh.model.User;

import java.util.List;

public interface UserService {

    User findUserProfileByJwt(String jwt) throws UserException;

    User findUserByEmail(String email) throws UserException;

    List<User> findAllUsers();

    List<User> getPenddingRestaurantOwner();

    void updatePassword(User user, String newPassword);

    void sendPasswordResetEmail(User user);

}
