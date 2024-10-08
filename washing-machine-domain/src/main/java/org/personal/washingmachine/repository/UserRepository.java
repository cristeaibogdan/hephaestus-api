package org.personal.washingmachine.repository;

import org.personal.washingmachine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
}
