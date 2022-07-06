package com.provod.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    public void init();

    public void saveNewEventImage(MultipartFile file, Long eventId) throws IOException;

    public void saveNewPlaceImage(MultipartFile file, Long placeId) throws IOException;

    public Resource loadEvent(Long eventId);

    public Resource loadPlace(Long placeId);

    public Resource loadPlaceholder();
}