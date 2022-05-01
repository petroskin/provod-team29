package com.provod.backend.service.impl;

import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import com.provod.backend.model.enums.TableType;
import com.provod.backend.model.exceptions.OneReservationPerEventException;
import com.provod.backend.repository.jpa.ReservationRepository;
import com.provod.backend.service.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService
{
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository)
    {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(User user, Event event, TableType tableType)
    {
        if (reservationRepository.findByUserAndEvent(user, event).isPresent())
            throw new OneReservationPerEventException();
        Reservation reservation = new Reservation(user, event, tableType);
        return reservationRepository.save(reservation);
    }

    @Override
    public Boolean removeReservation(Long id)
    {
        reservationRepository.deleteById(id);
        reservationRepository.flush();
        return !reservationRepository.existsById(id);
    }
}
