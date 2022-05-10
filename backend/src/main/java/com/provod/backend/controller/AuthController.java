package com.provod.backend.controller;

import com.provod.backend.model.User;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User retUser = userService.registerUser(user.getName(), user.getEmail(), user.getPhone(), user.getPassword());
            return ResponseEntity.ok(retUser);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // TODO: Login using JWT (or whatever we're using)
//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody User user) {
//        try {
//            // JWT Login ?
//            return ResponseEntity.internalServerError().build();
//        }
//        catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
}
