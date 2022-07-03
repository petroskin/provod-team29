package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.ReservationDTO;
import com.provod.backend.model.Event;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.User;
import com.provod.backend.model.exceptions.OneReservationPerEventException;
import com.provod.backend.service.EventService;
import com.provod.backend.service.ReservationService;
import com.provod.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final EventService eventService;

    public ReservationController(ReservationService reservationService, UserService userService, EventService eventService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservationsFromCurrentUser(HttpServletRequest request) {
        try {
            User user = userService.getUserByEmail(request.getRemoteUser());
            return ResponseEntity.ok(reservationService.getReservationByUser(user).stream().map(Reservation::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id){
        return ResponseEntity.ok(Reservation.convertToDTO(this.reservationService.getReservationById(id)));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUserById(id);
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reservationService.getReservationByUser(user));
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody ReservationDTO dto, HttpServletRequest request) {
        try {
            User user = userService.getUserById(dto.getUserId());
            Event event = eventService.getEvent(dto.getEventId());
            return ResponseEntity.ok(Reservation.convertToDTO(this.reservationService.createReservation(user, event, dto.getTableType())));
        } catch (OneReservationPerEventException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id)
    {
        try {
            if (reservationService.removeReservation(id)) {
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
