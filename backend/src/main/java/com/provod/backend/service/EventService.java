package com.provod.backend.service;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService
{
    Event createEvent(LocalDateTime start, Place place, MultipartFile image);
    Event updateEvent(Event event);
    Boolean removeEvent(Long id);
    Event getEvent(Long id);
    Event getEventWithReservations(Long id);
    List<Event> getAllEvents();
    List<Event> searchEventByStartAfter(LocalDateTime after);
    List<Event> searchEventByStartBetween(LocalDateTime from, LocalDateTime to);
    List<Event> getEventsByUserId(Long id);
    List<Event> getEventsByPlaceId(Long id);
}
