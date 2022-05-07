package com.provod.backend.model;

import com.provod.backend.model.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private List<PlaceOwner> placesOwned;
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Reservation> reservations;

    public User(String name, String email, String phone, String password)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = UserRole.Standard;
    }
}
