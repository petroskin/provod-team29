package com.provod.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.provod.backend.model.DTOs.UserDTO;
import com.provod.backend.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ProvodUser")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<PlaceOwner> placesOwned;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    public User(String name, String email, String phone, String password)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = UserRole.Standard;
    }

    public static UserDTO convertToDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .placesOwned(user.getPlacesOwned()
                        .stream()
                        .filter(placeOwner -> placeOwner.getOwner().getId().equals(user.getId()))
                        .map(PlaceOwner::getPlace).collect(Collectors.toList()))
                .reservations(user.getReservations())
                .build();
    }
}
