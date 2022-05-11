package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.JWTResponseDTO;
import com.provod.backend.model.DTOs.SignInDTO;
import com.provod.backend.model.DTOs.SignUpDTO;
import com.provod.backend.model.User;
import com.provod.backend.security.JWTUtils;
import com.provod.backend.service.UserService;
import com.provod.backend.service.impl.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        String role = roles.get(0);

        User user = userService.getUserById(userDetails.getId());

        return ResponseEntity.ok(new JWTResponseDTO(user.getId(),
                user.getName(),
                user.getEmail(),
                jwt,
                role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        User user;
        user = userService.registerUser(signUpDTO.getName(), signUpDTO.getEmail(), signUpDTO.getPhone(), signUpDTO.getPassword());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
