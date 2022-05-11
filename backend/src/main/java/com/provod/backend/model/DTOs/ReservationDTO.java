package com.provod.backend.model.DTOs;

import com.provod.backend.model.enums.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReservationDTO {
    Long id;
    Long userId;
    Long eventId;
    TableType tableType;
}
