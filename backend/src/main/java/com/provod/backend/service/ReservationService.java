package com.provod.backend.service;

import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import com.provod.backend.model.enums.TableType;

public interface ReservationService
{
    Reservation createReservation(User user, Event event, TableType tableType);
    Boolean removeReservation(Long id);
}
