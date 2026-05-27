package com.sudarsan.onlineexam.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sudarsan.onlineexam.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used for login)
    Optional<User> findByUsername(String username);

    // Find user by email (used during registration)
    Optional<User> findByEmail(String email);

    // Get all users with a specific role (e.g., STUDENT)
    List<User> findByRole(String role);

    // Get all users with a specific role and course
    List<User> findByRoleAndCourse(String role, String course);
}