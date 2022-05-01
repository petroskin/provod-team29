package com.provod.backend.service.impl;

import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.repository.jpa.PlaceOwnerRepository;
import com.provod.backend.repository.jpa.PlaceRepository;
import com.provod.backend.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaceServiceImpl implements PlaceService
{
    private final PlaceRepository placeRepository;
    private final PlaceOwnerRepository ownerRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository, PlaceOwnerRepository ownerRepository)
    {
        this.placeRepository = placeRepository;
        this.ownerRepository = ownerRepository;
    }

    //TODO poster should be image compatible data type
    @Override
    public Place createPlace(String name,
                             String description,
                             String address,
                             String city,
                             Integer latitude,
                             Integer longitude,
                             Integer standardCapacity,
                             Integer vipCapacity,
                             String placePoster)
    {
        Place place = new Place(name, description, address, city, latitude, longitude, standardCapacity, vipCapacity);
        place.setPlacePoster(placePoster);
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
    public Place getPlace(Long id)
    {
        return placeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public Place getPlaceWithEvents(Long id)
    {
        return placeRepository.findByIdWithEvents(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
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
        return placeRepository.findAllByNameLike(name);
    }

    @Override
    public List<Place> searchPlaceByCity(String city)
    {
        return placeRepository.findAllByCityLike(city);
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