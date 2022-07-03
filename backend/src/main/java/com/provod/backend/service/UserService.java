package com.provod.backend.service;

import com.provod.backend.model.User;
import com.provod.backend.model.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService
{
    User registerUser(String name, String email, String phone, String password);
    List<User> findAll();
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserWithPlacesOwned(Long id);
    User getUserWithReservations(Long id);
    User getUserWithReservationsAndPlacesOwned(Long id);
    User updateUser(User user);
    Boolean deleteUser(Long id);
    User setUserRole(User user, UserRole role);
}
