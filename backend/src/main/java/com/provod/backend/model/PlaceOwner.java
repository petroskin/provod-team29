package com.provod.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlaceOwner
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User owner;
    @ManyToOne
    private Place place;

    public PlaceOwner(User owner, Place place)
    {
        this.owner = owner;
        this.place = place;
    }
}
