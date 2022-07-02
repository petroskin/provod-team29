package com.provod.backend.web.rest;

import com.provod.backend.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//For providing images only
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class ImageController {
    private final ImageStorageService imageStorageService;
    @GetMapping("/event/{eventId}")
    public ResponseEntity<Resource> getEventImage(@PathVariable Long eventId){
        Resource file = null;
        try{
            file = imageStorageService.loadEvent(eventId);
        }
        catch (RuntimeException ignored){
            file = imageStorageService.loadEventPlaceholder();
        }
        return ResponseEntity.ok()
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<Resource> getPlaceImage(@PathVariable Long placeId){
        Resource file = null;
        try{
            file = imageStorageService.loadPlace(placeId);
        }
        catch (RuntimeException ignored){
            file = imageStorageService.loadPlacePlaceholder();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
