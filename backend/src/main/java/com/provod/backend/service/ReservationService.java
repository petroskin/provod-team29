package com.provod.backend.service;

import com.provod.backend.model.Reservation;

public interface ReservationService
{
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Reservation reservation);
    Reservation removeReservation(Reservation reservation);
}
