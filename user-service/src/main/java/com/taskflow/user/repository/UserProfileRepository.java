package com.taskflow.user.repository;

import com.taskflow.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByEmail(String email);

    List<UserProfile> findAllByActiveTrue();

    boolean existsById(UUID id);
}
