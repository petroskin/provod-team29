package com.provod.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.provod.backend.model.DTOs.EventDTO;
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
    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<Reservation> reservations;

    public Event(LocalDateTime start, Place place)
    {
        this.start = start;
        this.place = place;
    }

    public static EventDTO convertToDTO(Event event){
        return EventDTO.builder()
                .id(event.getId())
                .start(event.getStart())
                .placeId(event.getPlace().getId())
                .build();
    }
}
