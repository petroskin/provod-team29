package com.provod.backend.service.impl;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import com.provod.backend.model.exceptions.NightAlreadyRegisteredException;
import com.provod.backend.repository.jpa.EventRepository;
import com.provod.backend.service.EventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventServiceImpl implements EventService
{
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

    //TODO poster should be image compatible data type
    @Override
    public Event createEvent(LocalDateTime start, Place place, String eventPoster)
    {
        if (eventRepository.findByPlaceAndStartBetween(place, start.minusHours(6L), start.plusHours(6L)).isPresent())
            throw new NightAlreadyRegisteredException();
        Event event = new Event(start, place);
        event.setEventPoster(eventPoster);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event)
    {
        return eventRepository.save(event);
    }

    @Override
    public Boolean removeEvent(Long id)
    {
        eventRepository.deleteById(id);
        eventRepository.flush();
        return !eventRepository.existsById(id);
    }

    @Override
    public Event getEvent(Long id)
    {
        return eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public Event getEventWithReservations(Long id)
    {
        return eventRepository.findByIdWithReservations(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public List<Event> searchEventByStartAfter(LocalDateTime after)
    {
        return eventRepository.findAllByStartAfter(after);
    }

    @Override
    public List<Event> searchEventByStartBetween(LocalDateTime from, LocalDateTime to)
    {
        return eventRepository.findAllByStartBetween(from, to);
    }
}
