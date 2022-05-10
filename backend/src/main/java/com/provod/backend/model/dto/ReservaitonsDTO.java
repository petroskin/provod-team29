package com.provod.backend.model.dto;

import com.provod.backend.model.Event;
import com.provod.backend.model.enums.TableType;

import java.util.Date;

public class ReservaitonsDTO {
    public Date date;
    public TableType type;
    public Event event;

    public ReservaitonsDTO(Date date, int numberOfTables, TableType type, Event event) {
        this.date = date;
        this.type = type;
        this.event = event;
    }
}
