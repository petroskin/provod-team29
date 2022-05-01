package com.provod.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime start;
    @ManyToOne
    private Place place;
    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private List<Reservation> reservations;
    //TODO poster should be image compatible data type
    private String eventPoster;

    public Event(LocalDateTime start, Place place)
    {
        this.start = start;
        this.place = place;
    }
}
