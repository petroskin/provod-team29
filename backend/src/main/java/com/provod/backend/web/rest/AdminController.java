package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.EventDTO;
import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.DTOs.UserDTO;
import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import com.provod.backend.model.User;
import com.provod.backend.model.exceptions.EmailTakenException;
import com.provod.backend.model.exceptions.InvalidFieldException;
import com.provod.backend.model.exceptions.NightAlreadyRegisteredException;
import com.provod.backend.model.exceptions.PhoneNumberTakenException;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ImageStorageService;
import com.provod.backend.service.PlaceService;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PlaceService placeService;
    private final UserService userService;
    private final EventService eventService;
    private final ImageStorageService imageStorageService;

    public AdminController(PlaceService placeService, UserService userService, EventService eventService,  ImageStorageService imageStorageService) {
        this.placeService = placeService;
        this.userService = userService;
        this.eventService = eventService;
        this.imageStorageService = imageStorageService;
    }


    // TODO: Treda da se dodade PlaceID vo povik na front end
    @PostMapping("/add-owner")
    public ResponseEntity<Long> addOwner(@RequestParam Long placeId,@RequestParam String email){
        User user = userService.getUserByEmail(email);
        Place place = placeService.getPlace(placeId);
        placeService.addPlaceOwner(place, user);

        return ResponseEntity.ok(user.getId());
    }


    // TODO: Treba da se vidi sto se prakja od front end, falat dvata kapaciteti i posterot
    @PostMapping("/add-place")
    public ResponseEntity<Place> addPlace(@RequestParam PlaceDTO place) {
        Place ret = placeService.createPlace(place.getName(),
                                             place.getDescription(),
                                             place.getAddress(),
                                             place.getCity(),
                                             place.getLatitude(),
                                             place.getLongitude(),
                                             place.getStandardCapacity(),
                                             place.getVipCapacity(),
                                   null);

        return ret == null ? ResponseEntity.ok(ret) : ResponseEntity.internalServerError().build();
    }


    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> editUser(@PathVariable Long id, @RequestBody User updatedUser) {
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

    @PutMapping("/event/{id}")
    public ResponseEntity<EventDTO> editEvent(
            @PathVariable Long id,
            @RequestParam String start,
            @RequestParam Long placeId,
            @RequestPart(name = "image", required = false) MultipartFile image) {
        if (placeId == null || start == null) {
            return ResponseEntity.badRequest().build();
        }
        Place place;
        try {
            place = placeService.getPlace(placeId);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(421).build();
        }
        LocalDateTime startParsed;
        try {
            startParsed = LocalDateTime.parse(start.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        catch (DateTimeParseException e) {
            startParsed = LocalDateTime.parse(start.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        try {
            return ResponseEntity.ok(Event.convertToDTO(this.eventService.updateEvent(
                    id,
                    startParsed,
                    place,
                    image
            )));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (NightAlreadyRegisteredException e) {
            return ResponseEntity.status(422).build();
        }
    }

    @PutMapping("/place/{id}")
    public ResponseEntity<PlaceDTO> editPlace(@PathVariable Long id, @RequestBody PlaceDTO placeDTO) {
        try {
            return ResponseEntity.ok(Place.convertToDTO(placeService.updatePlace(id, placeDTO)));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok(id);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/place/{id}")
    public ResponseEntity<Long> deletePlace(@PathVariable Long id) {
        return placeService.removePlace(id) ? ResponseEntity.ok(id) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-owner/{id}")
    public ResponseEntity<Long> deleteOwner(@PathVariable Long id) {
        return placeService.removePlaceOwner(id) ? ResponseEntity.ok(id) : ResponseEntity.notFound().build();
    }

}
