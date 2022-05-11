package com.provod.backend.service;

import com.provod.backend.model.User;
import com.provod.backend.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    User registerUser(String name, String email, String phone, String password);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserWithPlacesOwned(Long id);
    User getUserWithReservations(Long id);
    User updateUser(User user);
    User setUserRole(User user, UserRole role);
    User getCurrentUser();
}
