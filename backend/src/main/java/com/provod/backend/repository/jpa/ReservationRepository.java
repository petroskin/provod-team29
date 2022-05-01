package com.provod.backend.repository.jpa;

import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>
{
    Optional<Reservation> findByUserAndEvent(User user, Event event);
}
