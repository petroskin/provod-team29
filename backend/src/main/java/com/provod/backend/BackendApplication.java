package com.provod.backend;

import com.provod.backend.service.ImageStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner
{
    public static void main(String[] args)
    {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        imageStorageService.init();
    }

    @Resource
    ImageStorageService imageStorageService;
}
