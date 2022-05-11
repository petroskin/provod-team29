package com.provod.backend.model.DTOs;

import com.provod.backend.model.Place;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class UserDTO {
    Long id;
    String name;
    String email;
    String phone;
    UserRole role;
    List<Place> placesOwned;
    List<Reservation> reservations;
}
