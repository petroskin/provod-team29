package com.provod.backend.service;

import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;

import java.util.List;

public interface PlaceService
{
    //TODO poster should be image compatible data type
    Place createPlace(String name,
                      String description,
                      String address,
                      String city,
                      Integer latitude,
                      Integer longitude,
                      Integer standardCapacity,
                      Integer vipCapacity,
                      String placePoster);
    Place updatePlace(Place place);
    Boolean removePlace(Long id);
    Place getPlace(Long id);
    Place getPlaceWithEvents(Long id);
    Place getPlaceWithOwners(Long id);
    Place getPlaceWithEventsAndOwners(Long id);
    List<Place> searchPlaceByName(String name);
    List<Place> searchPlaceByCity(String city);
    List<Place> searchPlaceByRatingGreaterEqual(Integer rating);
    List<Place> searchPlaceByRatingLessEqual(Integer rating);
    PlaceOwner addPlaceOwner(Place place, User owner);
    Boolean removePlaceOwner(Long id);
}
