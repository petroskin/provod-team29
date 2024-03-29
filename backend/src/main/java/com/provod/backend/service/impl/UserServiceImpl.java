package com.provod.backend.service.impl;

import com.provod.backend.model.User;
import com.provod.backend.model.enums.UserRole;
import com.provod.backend.model.exceptions.EmailTakenException;
import com.provod.backend.model.exceptions.InvalidFieldException;
import com.provod.backend.model.exceptions.PhoneNumberTakenException;
import com.provod.backend.repository.jpa.PlaceOwnerRepository;
import com.provod.backend.repository.jpa.PlaceRepository;
import com.provod.backend.repository.jpa.ReservationRepository;
import com.provod.backend.repository.jpa.UserRepository;
import com.provod.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final PlaceOwnerRepository placeOwnerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(String name, String email, String phone, String password)
    {
        checkNameValid(name);
        checkEmailValid(email);
        checkPhoneValid(phone);
        checkPasswordValid(password);
        checkEmailTaken(email);
        checkPhoneTaken(phone);
        User user = new User(name, email, phone, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id)
    {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public User getUserByEmail(String email)
    {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(email));
    }

    @Override
    public User getUserWithPlacesOwned(Long id)
    {
        return userRepository.findByIdWithPlacesOwned(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public User getUserWithReservations(Long id)
    {
        return userRepository.findByIdWithReservations(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
    }

    @Override
    public User getUserWithReservationsAndPlacesOwned(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id.toString()));
        user.setReservations(reservationRepository.findAllByUser(user));
        user.setPlacesOwned(placeOwnerRepository.findAllByOwner(user));
        return user;
    }

    @Override
    public User updateUser(User user)
    {
        User original = userRepository.getById(user.getId());
        checkNameValid(user.getName());
        checkEmailValid(user.getEmail());
        checkPhoneValid(user.getPhone());
        if (!Objects.equals(user.getEmail(), original.getEmail())) {
        checkEmailTaken(user.getEmail());
        }
        if (!Objects.equals(user.getPhone(), original.getPhone())) {
        checkPhoneTaken(user.getPhone());
        }
        original.setName(user.getName());
        original.setEmail(user.getEmail());
        original.setPhone(user.getPhone());
        if (user.getRole() != null) {
            original.setRole(user.getRole());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            checkPasswordValid(user.getPassword());
            original.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(original);
    }

    @Override
    public Boolean deleteUser(Long id)
    {
        userRepository.deleteById(id);
        userRepository.flush();
        return userRepository.findById(id).isEmpty();
    }

    @Override
    public User setUserRole(User user, UserRole role)
    {
        User original = userRepository.getById(user.getId());
        original.setRole(role);
        return userRepository.save(original);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.getByEmail(username);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }

    private void checkNameValid(String name)
    {
        if (!name.matches("\\w(\\w| ){0,49}"))
            throw new InvalidFieldException("name");
    }

    private void checkEmailValid(String email)
    {
        if (!email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+"))
            throw new InvalidFieldException("email");

    }

    private void checkPhoneValid(String phone)
    {
        if (!phone.matches("\\+?[0-9 ]+"))
            throw new InvalidFieldException("phone");
    }

    private void checkPasswordValid(String password)
    {
        if (!password.matches(".{8,30}"))
            throw new InvalidFieldException("password");
    }

    private void checkEmailTaken(String email)
    {
        if (userRepository.findByEmail(email).isPresent())
            throw new EmailTakenException();
    }

    private void checkPhoneTaken(String phone)
    {
        if (userRepository.findByPhone(phone).isPresent())
            throw new PhoneNumberTakenException();
    }


}
