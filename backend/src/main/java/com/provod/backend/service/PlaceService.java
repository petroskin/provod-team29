package com.provod.backend.service;

import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;

import java.util.List;

public interface PlaceService
{
    Place createPlace(Place place);
    Place updatePlace(Place place);
    Place removePlace(Place place);
    Place getPlace(Long id);
    Place getPlaceWithEvents(Long id);
    Place getPlaceWithOwners(Long id);
    Place getPlaceWithEventsAndOwners(Long id);
    List<Place> searchPlaceByName(String name);
    List<Place> searchPlaceByCity(String city);
    List<Place> searchPlaceByRatingGreaterEqual(Integer rating);
    List<Place> searchPlaceByRatingLessEqual(Integer rating);
    PlaceOwner addPlaceOwner(Place place, User owner);
    PlaceOwner removePlaceOwner(Place place, User owner);
}
