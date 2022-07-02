package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.DTOs.UserDTO;
import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.model.exceptions.EmailTakenException;
import com.provod.backend.model.exceptions.InvalidFieldException;
import com.provod.backend.model.exceptions.PhoneNumberTakenException;
import com.provod.backend.service.PlaceService;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
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

    @GetMapping("/{id}/reservationsandplaces")
    public ResponseEntity<UserDTO> getUserWithReservationsAndPlacesOwned(@PathVariable Long id) {
        try {
            User user = userService.getUserWithReservationsAndPlacesOwned(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        try {
            if (updatedUser.getId() != null && updatedUser.getId() != 0) {
                return ResponseEntity.badRequest().build();
            }
            updatedUser.setId(id);
            User user = this.userService.updateUser(updatedUser);
            return ResponseEntity.ok(User.convertToDTO(user));
        }
        catch (InvalidFieldException e) {
            String errorMessage;
            switch (e.getMessage())
            {
                case "name":
                    errorMessage = "Name must contain only alphanumeric characters and underscore, and is limited to 50 characters.";
                    break;
                case "email":
                    errorMessage = "Email must be in the form x@y.z.";
                    break;
                case "phone":
                    errorMessage = "Phone must consist of numbers and maybe + at the beginning.";
                    break;
                case "password":
                    errorMessage = "Password must be between 8 and 30 characters long.";
                    break;
                default:
                    errorMessage = "Unknown error.";
            }
            return ResponseEntity.badRequest().build();
        }
        catch (EmailTakenException e) {
            return ResponseEntity.status(418).build();
        }
        catch (PhoneNumberTakenException e) {
            return ResponseEntity.status(419).build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
