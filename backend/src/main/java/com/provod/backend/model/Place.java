package com.provod.backend.model;

import com.provod.backend.model.DTOs.PlaceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Place
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Integer standardCapacity;
    private Integer vipCapacity;
    private Integer rating;
    //TODO poster should be image compatible data type
    private String placePoster;
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<Event> events;
    @OneToMany(mappedBy = "place", orphanRemoval = true)
    private List<PlaceOwner> owners;

    public Place(String name, String description, String address, String city, Double latitude, Double longitude, Integer standardCapacity, Integer vipCapacity)
    {
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.standardCapacity = standardCapacity;
        this.vipCapacity = vipCapacity;
    }

    public static PlaceDTO convertToDTO(Place place){
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .address(place.getAddress())
                .city(place.getCity())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .standardCapacity(place.getStandardCapacity())
                .vipCapacity(place.getVipCapacity())
                .rating(place.getRating())
                .eventIds(place.getEvents().stream().map(Event::getId).collect(Collectors.toList()))
                .ownerIds(place
                        .getOwners()
                        .stream()
                        .filter(placeOwner -> placeOwner.getPlace().getId().equals(place.getId()))
                        .map(placeOwner -> placeOwner.getOwner().getId()).collect(Collectors.toList()))
                .build();
    }
}
