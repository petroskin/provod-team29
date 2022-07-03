package com.provod.backend.web.rest;

import com.provod.backend.model.Event;
import com.provod.backend.model.DTOs.EventDTO;
import com.provod.backend.model.exceptions.NightAlreadyRegisteredException;
import com.provod.backend.service.EventService;
import com.provod.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private final PlaceService placeService;

    public EventController(EventService eventService, PlaceService placeService) {
        this.eventService = eventService;
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(this.eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        Event event = this.eventService.getEvent(id);
        return ResponseEntity.ok(Event.convertToDTO(event));
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(
            @RequestParam String start,
            @RequestParam Long placeId,
            @RequestPart(name = "image", required = false) MultipartFile image) {
        if (placeId == null || start == null) {
            return ResponseEntity.badRequest().build();
        }
        try
        {
            return ResponseEntity.ok(Event.convertToDTO(this.eventService.createEvent(
                    LocalDateTime.parse(start.replace('T', ' '), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    placeService.getPlace(placeId), image)));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(421).build();
        }
        catch (NightAlreadyRegisteredException e) {
            return ResponseEntity.status(422).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteEvent(@PathVariable Long id) {
        Boolean isSuccess;
        try
        {
            isSuccess = this.eventService.removeEvent(id);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(id);
        }
        return isSuccess ? ResponseEntity.ok(id) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Event>> getEventsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(this.eventService.getEventsByUserId(id));
    }

    @GetMapping("/place/{id}")
    public ResponseEntity<List<Event>> getEventsByPlaceId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(this.eventService.getEventsByPlaceId(id));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
