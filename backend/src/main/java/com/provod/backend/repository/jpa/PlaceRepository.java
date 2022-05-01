package com.provod.backend.repository.jpa;

import com.provod.backend.model.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>
{
    List<Place> findAllByNameLike(String placeName);

    List<Place> findAllByCityLike(String cityName);

    List<Place> findAllByRatingGreaterThanEqual(Integer rating);

    List<Place> findAllByRatingLessThanEqual(Integer rating);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"events"})
    @Query("select p from Place p where p.id = :id")
    Optional<Place> findByIdWithEvents(@Param("id") Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"owners"})
    @Query("select p from Place p where p.id = :id")
    Optional<Place> findByIdWithOwners(@Param("id") Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"events", "owners"})
    @Query("select p from Place p where p.id = :id")
    Optional<Place> findByIdWithEventsAndOwners(@Param("id") Long id);
}
