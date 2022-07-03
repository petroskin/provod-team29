package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.Place;
import com.provod.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clubs")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces(@RequestParam(required = false) String city, @RequestParam(required = false) String name) {
        List<Place> places;
        if (name != null && !name.isEmpty()) {
            places = placeService.searchPlaceByName(name);
        }
        else if (city != null && !city.isEmpty()) {
            places = placeService.searchPlaceByCity(city);
        }
        else {
            places = placeService.getAllPlaces();
        }
        return ResponseEntity.ok(places.stream().map(Place::convertToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<PlaceDTO> getPlaceByIdWithEvents(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Place.convertToDTO(this.placeService.getPlaceWithEvents(id)));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Place.convertToDTO(this.placeService.getPlace(id)));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
