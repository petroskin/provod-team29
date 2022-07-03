package com.provod.backend.model.DTOs;

import com.provod.backend.model.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class PlaceDTO {
    Long id;
    String name;
    String description;
    String address;
    String city;
    Double latitude;
    Double longitude;
    Integer standardCapacity;
    Integer vipCapacity;
    Integer rating;
    List<Event> events;
    List<Long> ownerIds;
}
