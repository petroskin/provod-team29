package com.provod.backend.repository.jpa;

import com.provod.backend.model.Place;
import com.provod.backend.model.PlaceOwner;
import com.provod.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceOwnerRepository extends JpaRepository<PlaceOwner, Long>
{
    List<PlaceOwner> findAllByOwner(User owner);
    List<PlaceOwner> findAllByPlace(Place place);
}
