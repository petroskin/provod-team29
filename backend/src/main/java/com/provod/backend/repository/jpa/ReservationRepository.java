package com.provod.backend.repository.jpa;

import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>
{
    Optional<Reservation> findByUserAndEvent(User user, Event event);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"reservations"})
    @Query("select r from Reservation r where r.user.id = :id")
    Optional<List<Reservation>> findByUserId(@Param("id") Long id);

    List<Reservation> findAllByUser(User user);
}
