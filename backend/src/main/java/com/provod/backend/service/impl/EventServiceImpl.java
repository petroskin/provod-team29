package com.provod.backend.service.impl;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.model.exceptions.NightAlreadyRegisteredException;
import com.provod.backend.repository.jpa.*;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ImageStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService
{
    private final EventRepository eventRepository;
    private final PlaceOwnerRepository placeOwnerRepository;
    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;
    private final ImageStorageService imageStorageService;

    @Override
    public Event createEvent(LocalDateTime start, Place place, MultipartFile image)
    {
        if (eventRepository.findByPlaceAndStartBetween(place, start.minusHours(6L), start.plusHours(6L)).isPresent())
            throw new NightAlreadyRegisteredException();
        Event event = new Event(start, place);
        Event savedEvent = eventRepository.save(event);
        if(image != null){
            try{
                imageStorageService.saveNewEventImage(image, savedEvent.getId());
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
        Event event = eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
        reservationRepository.deleteAll(reservationRepository.findAllByEvent(event));
        eventRepository.deleteById(id);
        reservationRepository.flush();
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

    @Override
    public List<Event> getEventsByUserId(Long id)
    {
        return placeOwnerRepository
                .findAll()
                .stream()
                .filter(i -> i.getOwner().getId().equals(id))
                .flatMap(i -> placeRepository.findAllByOwnersContaining(i).stream())
                .flatMap(i -> eventRepository.findAllByPlace(i).stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsByPlaceId(Long id)
    {
        Place place = placeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
        return eventRepository.findAllByPlace(place);
    }
}
