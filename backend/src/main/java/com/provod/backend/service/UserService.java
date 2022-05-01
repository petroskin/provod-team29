package com.provod.backend.service;

import com.provod.backend.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    User registerUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserWithPlacesOwned(Long id);
    User getUserWithReservations(Long id);
    User updateUser(User user);
}
