package com.provod.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    private Integer latitude;
    private Integer longitude;
    private Integer standardCapacity;
    private Integer vipCapacity;
    private Integer rating;
    // should be image compatible data type
    private String placePoster;
    @OneToMany(mappedBy = "place")
    private List<Event> events;
    @OneToMany(mappedBy = "place")
    private List<PlaceOwner> owners;

    public Place(String name, String description, String address, String city, Integer latitude, Integer longitude, Integer standardCapacity, Integer vipCapacity)
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
}
