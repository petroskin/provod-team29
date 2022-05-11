package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.DTOs.UserDTO;
import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.service.PlaceService;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(User.convertToDTO(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/clubs/{id}")
    public ResponseEntity<List<PlaceDTO>> getClubsFromUser(@PathVariable Long id){
        try {
            List<Place> places  = this.userService.getUserWithPlacesOwned(id).getPlacesOwned().stream()
                    .map(PlaceOwner::getPlace)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(places.stream().map(Place::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
