package com.provod.backend.service.impl;

import com.provod.backend.model.DTOs.PlaceDTO;
import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import com.provod.backend.repository.jpa.EventRepository;
import com.provod.backend.repository.jpa.PlaceOwnerRepository;
import com.provod.backend.repository.jpa.PlaceRepository;
import com.provod.backend.service.ImageStorageService;
import com.provod.backend.service.PlaceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService
{
    private final PlaceRepository placeRepository;
    private final PlaceOwnerRepository ownerRepository;
    private final EventRepository eventRepository;
    private final ImageStorageService imageStorageService;

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
    public Place updatePlace(Long id, PlaceDTO placeDTO)
    {
        Place place = placeRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
        place.setName(placeDTO.getName());
        place.setDescription(placeDTO.getDescription());
        place.setAddress(placeDTO.getAddress());
        place.setCity(placeDTO.getCity());
        place.setLatitude(placeDTO.getLatitude());
        place.setLongitude(placeDTO.getLongitude());
        place.setStandardCapacity(placeDTO.getStandardCapacity());
        place.setVipCapacity(placeDTO.getVipCapacity());
        place.setRating(placeDTO.getRating());
        return placeRepository.save(place);
    }

    @Override
    public Boolean removePlace(Long id)
    {
        Place place = placeRepository.findById(id).orElse(null);
        if (place == null) {
            return false;
        }
        ownerRepository.deleteAll(ownerRepository.findAllByPlace(place));
        placeRepository.deleteById(id);
        ownerRepository.flush();
        placeRepository.flush();
        return !placeRepository.existsById(id);
    }

    @Override
    public List<Place> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        places.forEach(i -> i.setOwners(ownerRepository.findAllByPlace(i)));
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
        if (ownerRepository.findAllByPlace(place).stream().anyMatch(i -> i.getOwner().getId().equals(owner.getId()))) {
            return null;
        }
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
