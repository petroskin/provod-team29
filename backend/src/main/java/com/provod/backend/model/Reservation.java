package com.provod.backend.model;

import com.provod.backend.model.enums.TableType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Event event;
    @Enumerated(value = EnumType.STRING)
    private TableType tableType;

    public Reservation(User user, Event event, TableType tableType)
    {
        this.user = user;
        this.event = event;
        this.tableType = tableType;
    }
}
