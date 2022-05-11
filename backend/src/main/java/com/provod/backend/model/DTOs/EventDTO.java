package com.provod.backend.model.DTOs;

import com.provod.backend.model.Place;
import com.provod.backend.model.Reservation;
import com.provod.backend.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class EventDTO {
    Long id;
    LocalDateTime start;
    Long placeId;
}
