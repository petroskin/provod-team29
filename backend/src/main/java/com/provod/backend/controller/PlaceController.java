package com.provod.backend.controller;

import com.provod.backend.model.Place;
import com.provod.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces(@RequestBody String city) {
            return this.getAllPlacesInACity(city);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Place>> getAllPlacesInACity(@RequestBody String city) {
        return ResponseEntity.ok(this.placeService.searchPlaceByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(this.placeService.getPlace(id));
    }
}
