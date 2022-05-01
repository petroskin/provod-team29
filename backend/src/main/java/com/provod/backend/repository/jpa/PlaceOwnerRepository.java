package com.provod.backend.repository.jpa;

import com.provod.backend.model.PlaceOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceOwnerRepository extends JpaRepository<PlaceOwner, Long>
{
}
