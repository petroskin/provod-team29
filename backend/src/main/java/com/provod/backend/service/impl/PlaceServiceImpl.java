package com.provod.backend.service.impl;

import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.repository.jpa.EventRepository;
import com.provod.backend.repository.jpa.PlaceOwnerRepository;
import com.provod.backend.repository.jpa.PlaceRepository;
import com.provod.backend.service.ImageStorageService;
import com.provod.backend.service.PlaceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaceServiceImpl implements PlaceService
{
    private final PlaceRepository placeRepository;
    private final PlaceOwnerRepository ownerRepository;
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;

    public PlaceServiceImpl(PlaceRepository placeRepository, PlaceOwnerRepository ownerRepository, EventRepository eventRepository, ImageStorageService imageStorageService)
    {
        this.placeRepository = placeRepository;
        this.ownerRepository = ownerRepository;
        this.eventRepository = eventRepository;
        this.imageStorageService = imageStorageService;
    }

    //TODO poster should be image compatible data type
    @Override
    public Place createPlace(String name,
                             String description,
                             String address,
                             String city,
                             Double latitude,
                             Double longitude,
                             Integer standardCapacity,
                             Integer vipCapacity,
                             MultipartFile placePoster)
    {
        Place place = new Place(name, description, address, city, latitude, longitude, standardCapacity, vipCapacity);
        if (placePoster != null) {
            try {
                imageStorageService.saveNewPlaceImage(placePoster, place.getId());
            }
            catch (IOException ignore) {
                // ne bi trebalo da se desi
            }
        }
        return placeRepository.save(place);
    }

    @Override
    public Place updatePlace(Place place)
    {
        return placeRepository.save(place);
    }

    @Override
    public Boolean removePlace(Long id)
    {
        placeRepository.deleteById(id);
        placeRepository.flush();
        return !placeRepository.existsById(id);
    }

    @Override
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @Override
    public Place getPlace(Long id)
    {
        return placeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public Place getPlaceWithEvents(Long id)
    {
        Place place = placeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
        place.setEvents(eventRepository.findAllByPlace(place));
        return place;
    }

    @Override
    public Place getPlaceWithOwners(Long id)
    {
        return placeRepository.findByIdWithOwners(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public Place getPlaceWithEventsAndOwners(Long id)
    {
        return placeRepository.findByIdWithEventsAndOwners(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public List<Place> searchPlaceByName(String name)
    {
        return placeRepository.findByName(name.toLowerCase());
    }

    @Override
    public List<Place> searchPlaceByCity(String city)
    {
        return placeRepository.findByCity(city.toLowerCase());
    }


    @Override
    public List<Place> searchPlaceByRatingGreaterEqual(Integer rating)
    {
        return placeRepository.findAllByRatingGreaterThanEqual(rating);
    }

    @Override
    public List<Place> searchPlaceByRatingLessEqual(Integer rating)
    {
        return placeRepository.findAllByRatingLessThanEqual(rating);
    }

    @Override
    public PlaceOwner addPlaceOwner(Place place, User owner)
    {
        PlaceOwner placeOwner = new PlaceOwner(owner, place);
        return ownerRepository.save(placeOwner);
    }

    @Override
    public Boolean removePlaceOwner(Long id)
    {
        ownerRepository.deleteById(id);
        ownerRepository.flush();
        return !ownerRepository.existsById(id);
    }
}
