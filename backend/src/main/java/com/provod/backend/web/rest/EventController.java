package com.provod.backend.web.rest;

import com.provod.backend.model.Event;
import com.provod.backend.model.DTOs.EventDTO;
import com.provod.backend.service.EventService;
import com.provod.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final PlaceService placeService;

    public EventController(EventService eventService, PlaceService placeService) {
        this.eventService = eventService;
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(this.eventService.getAllEvents().stream().map(Event::convertToDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Event event = this.eventService.getEvent(id);
        return ResponseEntity.ok(Event.convertToDTO(event));
    }

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestPart EventDTO eventDTO, @RequestPart(required = false) MultipartFile image) {
        Event retVal;
        if (eventDTO != null) {
            retVal = this.eventService.createEvent(eventDTO.getStart(), placeService.getPlace(eventDTO.getPlaceId()), image);
        }

        else return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(Event.convertToDTO(retVal));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteEvent(@PathVariable Long id) {
        Boolean isSuccess =  this.eventService.removeEvent(id);
        return isSuccess ? ResponseEntity.ok(id) : ResponseEntity.badRequest().build();
    }

}
