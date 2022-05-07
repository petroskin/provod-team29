package com.provod.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {
    public void init();

    public void saveNewImage(MultipartFile file, Long eventId) throws IOException;

    public Resource load(Long eventId);

    public Resource loadPlaceholder();
}