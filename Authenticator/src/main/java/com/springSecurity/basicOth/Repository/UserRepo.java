package com.springSecurity.basicOth.Repository;

import com.springSecurity.basicOth.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);
}
