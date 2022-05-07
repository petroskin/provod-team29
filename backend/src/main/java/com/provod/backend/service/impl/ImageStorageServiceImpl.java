package com.provod.backend.service.impl;

import com.provod.backend.service.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageStorageServiceImpl implements ImageStorageService {
    private final Path root = Paths.get("images");

    @Override
    public void init() {
        try {
            if(!Files.exists(root))
                Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for images!");
        }
    }

    @Override
    public void saveNewImage(MultipartFile file, Long eventId) throws IOException {
        File imageDir = new File(String.valueOf(this.root.resolve(eventId.toString())));
        if(!Files.exists(imageDir.toPath())){
            Files.createDirectory(imageDir.toPath());
        }
        BufferedImage image = ImageIO.read(file.getInputStream());

        //maksimum rezolucija za slika shto se chuva da e 1000 pikseli za podolgata strana
        BufferedImage finalImage = null;
        if(image.getWidth() >= image.getHeight()){
            double aspectRatio = (double) image.getHeight() / image.getWidth();
            Image temp = image.getScaledInstance(1000, (int) (1000 * aspectRatio), Image.SCALE_SMOOTH);
            finalImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics().drawImage(temp, 0, 0, Color.WHITE, null);
        }
        else{
            double aspectRatio = (double) image.getWidth() / image.getHeight();
            Image temp = image.getScaledInstance((int) (1000 * aspectRatio), 1000, Image.SCALE_SMOOTH);
            finalImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics().drawImage(temp, 0, 0, Color.WHITE, null);
        }
        File outputFile = new File(String.valueOf(this.root.resolve(eventId + ".jpg")));
        outputFile.getParentFile().mkdirs();
        ImageIO.write(finalImage,"jpg", outputFile);
    }

    @Override
    public Resource load(Long eventId) {
        try {
            Path file = root.resolve(eventId + ".jpg");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Resource loadPlaceholder(){
        try {
            Path file = root.resolve("placeholder.jpg");
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
