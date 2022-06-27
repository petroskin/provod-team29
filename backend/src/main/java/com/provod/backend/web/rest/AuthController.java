package com.provod.backend.web.rest;

import com.provod.backend.model.DTOs.JWTResponseDTO;
import com.provod.backend.model.DTOs.SignInDTO;
import com.provod.backend.model.DTOs.SignUpDTO;
import com.provod.backend.model.User;
import com.provod.backend.model.exceptions.EmailTakenException;
import com.provod.backend.model.exceptions.InvalidFieldException;
import com.provod.backend.model.exceptions.PhoneNumberTakenException;
import com.provod.backend.security.JWTUtils;
import com.provod.backend.service.UserService;
import com.provod.backend.service.impl.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
public class AuthController
{

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTUtils jwtUtils)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInDto)
    {
        Authentication authentication;
        try
        {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword()));
        }
        catch (BadCredentialsException e)
        {
            return ResponseEntity.badRequest().body("Bad credentials.");
        }

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
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDTO)
    {
        try
        {
            userService.registerUser(signUpDTO.getName(), signUpDTO.getEmail(), signUpDTO.getPhone(), signUpDTO.getPassword());
            return ResponseEntity.ok().build();
        }
        catch (InvalidFieldException e)
        {
            String errorMessage;
            switch (e.getMessage())
            {
                case "name":
                    errorMessage = "Name must contain only alphanumeric characters and underscore, and is limited to 50 characters.";
                    break;
                case "email":
                    errorMessage = "Email must be in the form x@y.z.";
                    break;
                case "phone":
                    errorMessage = "Phone must consist of numbers and maybe + at the beginning.";
                    break;
                case "password":
                    errorMessage = "Password must be between 8 and 30 characters long.";
                    break;
                default:
                    errorMessage = "Unknown error.";
            }
            return ResponseEntity.badRequest().body(errorMessage);
        }
        catch (EmailTakenException e)
        {
            return ResponseEntity.badRequest().body("That email is taken");
        }
        catch (PhoneNumberTakenException e)
        {
            return ResponseEntity.badRequest().body("That phone number is taken.");
        }
    }
}
