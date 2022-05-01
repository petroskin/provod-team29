package com.provod.backend.service;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService
{
    //TODO poster should be image compatible data type
    Event createEvent(LocalDateTime start, Place place, String eventPoster);
    Event updateEvent(Event event);
    Boolean removeEvent(Long id);
    Event getEvent(Long id);
    Event getEventWithReservations(Long id);
    List<Event> searchEventByStartAfter(LocalDateTime after);
    List<Event> searchEventByStartBetween(LocalDateTime from, LocalDateTime to);
}
