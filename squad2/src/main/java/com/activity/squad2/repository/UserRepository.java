package com.activity.squad2.repository;

import com.activity.squad2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}