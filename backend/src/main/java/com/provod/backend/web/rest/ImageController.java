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
@RequestMapping("/api/events/images")
@CrossOrigin(origins = "*")
public class ImageController {
    private final ImageStorageService imageStorageService;
    @GetMapping("/{eventId}")
    public ResponseEntity<Resource> getImage(@PathVariable Long eventId){
        Resource file = null;
        try{
            file = imageStorageService.load(eventId);
        }
        catch (RuntimeException ignored){
            file = imageStorageService.loadPlaceholder();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
