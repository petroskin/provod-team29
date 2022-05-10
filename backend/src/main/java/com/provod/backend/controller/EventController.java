package com.provod.backend.controller;

import com.provod.backend.model.Event;
import com.provod.backend.model.dto.EventDTO;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ImageStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(this.eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = this.eventService.getEvent(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO dto) {
        Event retVal;
        if (dto != null) {
            retVal = this.eventService.createEvent(dto.start, dto.place, dto.eventPoster);
        }

        else return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(retVal);
    }

}
