package com.provod.backend.controller;

import com.provod.backend.model.Place;
import com.provod.backend.model.User;
import com.provod.backend.service.PlaceService;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PlaceService placeService;

    public UserController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // TODO Add a service function ???
    @GetMapping("/clubs/{id}")
    public ResponseEntity<List<Place>> getClubsFromUser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(this.placeService.getPlaceWithOwners(id));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
