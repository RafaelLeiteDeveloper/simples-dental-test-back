package com.simplesdental.product.infrastructure.repository;

import com.simplesdental.product.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNameIgnoreCase(String name);
    Optional<User> findByEmailIgnoreCase(String email);

    default void existsByNameThenThrow(String name, Long id){
        this.findByNameIgnoreCase(name)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                                                  "Name already exists");
            });
    }

    default void existsByEmailThenThrow(String email, Long id){
        this.findByEmailIgnoreCase(email)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Email already exists");
            });
    }

    default User getByIdOrElseThrow(Long id){
        return this.findById(id)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    default User getUserByEmailOrElseThrow(String email){
        return this.findByEmailIgnoreCase(email)
                   .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}
