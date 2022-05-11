package com.provod.backend.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SignInDTO {

    @NotNull
    @NotEmpty
    @NotBlank
    private final String email;

    @NotNull
    @NotEmpty
    @NotBlank
    private final String password;
}