package com.provod.backend.model.DTOs;

import lombok.Getter;

@Getter
public class JWTResponseDTO {
    private final Long id;
    private final String name;
    private final String email;
    private final String token;
    private final String role;
    private final String type = "Bearer";

    public JWTResponseDTO(Long id, String name, String email, String token,
                          String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
        this.role = role;
    }
}