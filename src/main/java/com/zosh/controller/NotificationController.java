package com.zosh.controller;

import com.zosh.Exception.UserException;
import com.zosh.model.Notification;
import com.zosh.model.User;
import com.zosh.service.NotificationService;
import com.zosh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationSerivce;
    @Autowired
    private UserService userService;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> findUsersNotification(
            @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);

        List<Notification> notifications = notificationSerivce.findUsersNotification(user.getId());
        return new ResponseEntity<List<Notification>>(notifications, HttpStatus.ACCEPTED);
    }

}
