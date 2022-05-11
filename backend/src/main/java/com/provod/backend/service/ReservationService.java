package com.provod.backend.service;

import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import com.provod.backend.model.enums.TableType;

import java.util.List;

public interface ReservationService
{
    Reservation getReservationById(Long id);
    Reservation createReservation(User user, Event event, TableType tableType);
    List<Reservation> getReservationByUser(User user);
    Boolean removeReservation(Long id);
}
