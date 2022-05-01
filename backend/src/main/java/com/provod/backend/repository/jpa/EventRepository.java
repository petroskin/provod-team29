package com.provod.backend.repository.jpa;

import com.provod.backend.model.Event;
import com.provod.backend.model.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>
{
    List<Event> findAllByStartAfter(LocalDateTime after);

    List<Event> findAllByStartBetween(LocalDateTime from, LocalDateTime to);

    Optional<Event> findByPlaceAndStartBetween(Place place, LocalDateTime from, LocalDateTime to);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"reservations"})
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdWithReservations(@Param("id") Long id);
}
