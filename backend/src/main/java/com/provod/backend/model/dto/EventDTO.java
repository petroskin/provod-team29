package com.provod.backend.model.dto;

import com.provod.backend.model.Place;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class EventDTO {

    public LocalDateTime start;
    public Place place;
    public MultipartFile eventPoster;

}
