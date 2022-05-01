package com.provod.backend.repository.jpa;

import com.provod.backend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);
    User getByEmail(String email);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"placesOwned"})
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithPlacesOwned(@Param("id") Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"reservations"})
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdWithReservations(@Param("id") Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"placesOwned"})
    @Query("select u from User u where u.id = :id")
    User getByIdWithPlacesOwned(@Param("id") Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,
            attributePaths = {"reservations"})
    @Query("select u from User u where u.id = :id")
    User getByIdWithReservations(@Param("id") Long id);
}
