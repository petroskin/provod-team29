package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import com.provod.backend.model.User;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ImageStorageService;
import com.provod.backend.service.PlaceService;
import com.provod.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
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


    @PutMapping("/edit-user/{id}")
    public ResponseEntity<Long> editUser(@PathVariable Long id, @RequestParam String name, @RequestParam String email, @RequestParam String phone) {
        User user = userService.getUserById(id);
        if (user == null) return ResponseEntity.notFound().build();
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        User ret = this.userService.updateUser(user);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/edit-event/{id}")
    public ResponseEntity<Long> editEvent(@PathVariable Long id, @RequestParam MultipartFile poster, @RequestParam PlaceDTO place, @RequestParam LocalDateTime date) {
        Event event = eventService.getEvent(id);
        if (event == null) return ResponseEntity.notFound().build();
        event.setPlace(new Place(place.getName(), place.getDescription(), place.getAddress(), place.getCity(), place.getLatitude(), place.getLongitude(), 0, 0));
        event.setStart(date);
        Event ret = this.eventService.updateEvent(event);

        if (ret == null) return ResponseEntity.internalServerError().build();

        // update image poster
        try {
            imageStorageService.saveNewEventImage(poster, id);
        } catch (IOException exception) {
            return  ResponseEntity.internalServerError().build();
        }


        return ResponseEntity.ok(id);
    }

    @PutMapping("/edit-place/{id}")
    public ResponseEntity<Long> editPlace(@PathVariable Long id,
                                          @RequestParam String name,
                                          @RequestParam String description,
                                          @RequestParam String address,
                                          @RequestParam String city,
                                          @RequestParam double latitude,
                                          @RequestParam double longitude,
                                          @RequestParam int rating) {
        Place place = placeService.getPlace(id);
        if (place == null) return ResponseEntity.badRequest().build();

        place.setName(name);
        place.setDescription(description);
        place.setAddress(address);
        place.setCity(city);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setRating(rating);

        Place ret = placeService.updatePlace(place);

        return place != null ? ResponseEntity.ok(id) : ResponseEntity.internalServerError().build();
    }

    // TODO: Nema komanda vo UserService
//
//    @DeleteMapping("delete-user/{id}")
//    public ResponseEntity<Long> deleteUser(@PathVariable Long id) {
//
//    }

    @DeleteMapping("/delete-place/{id}")
    public ResponseEntity<Long> deletePlace(@PathVariable Long id) {
        return placeService.removePlace(id) ? ResponseEntity.ok(id) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-owner/{id}")
    public ResponseEntity<Long> deleteOwner(@PathVariable Long id) {
        return placeService.removePlaceOwner(id) ? ResponseEntity.ok(id) : ResponseEntity.notFound().build();
    }

}
