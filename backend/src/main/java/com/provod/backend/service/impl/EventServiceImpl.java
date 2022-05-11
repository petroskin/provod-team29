package com.provod.backend.service.impl;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import com.provod.backend.model.exceptions.NightAlreadyRegisteredException;
import com.provod.backend.repository.jpa.EventRepository;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventServiceImpl implements EventService
{
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;

    public EventServiceImpl(EventRepository eventRepository, ImageStorageService imageStorageService) {
        this.eventRepository = eventRepository;
        this.imageStorageService = imageStorageService;
    }

    @Override
    public Event createEvent(LocalDateTime start, Place place, MultipartFile image)
    {
        if (eventRepository.findByPlaceAndStartBetween(place, start.minusHours(6L), start.plusHours(6L)).isPresent())
            throw new NightAlreadyRegisteredException();
        Event event = new Event(start, place);
        Event savedEvent = eventRepository.save(event);
        if(image != null){
            try{
                imageStorageService.saveNewImage(image, savedEvent.getId());
            }
            catch (IOException ignore){
                //ne bi trebalo da se desi ova
            }
        }
        return savedEvent;
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
    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
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
