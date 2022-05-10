package com.provod.backend.controller;

import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import com.provod.backend.model.dto.ReservaitonsDTO;
import com.provod.backend.model.exceptions.OneReservationPerEventException;
import com.provod.backend.service.ReservationService;
import com.provod.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservationsFromCurrentUser() {
        try {
            User user = userService.getCurrentUser();

            return ResponseEntity.ok(reservationService.getReservationByUser(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id){
        return ResponseEntity.ok(this.reservationService.getReservationById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservaitonsDTO dto) {
        try {
            User user = userService.getCurrentUser();

            return ResponseEntity.ok(this.reservationService.createReservation(user, dto.event, dto.type));
        } catch (OneReservationPerEventException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
