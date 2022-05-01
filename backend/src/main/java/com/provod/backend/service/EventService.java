package com.provod.backend.service;

import com.provod.backend.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService
{
    Event createEvent(Event event);
    Event updateEvent(Event event);
    Event removeEvent(Event event);
    Event getEvent(Long id);
    Event getEventWithReservations(Long id);
    List<Event> searchEventByStartAfter(LocalDateTime after);
    List<Event> searchEventByStartBetween(LocalDateTime from, LocalDateTime to);
}
