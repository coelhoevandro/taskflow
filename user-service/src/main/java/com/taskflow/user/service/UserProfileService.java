package com.taskflow.user.service;

import com.taskflow.user.dto.CreateProfileRequest;
import com.taskflow.user.dto.UpdateProfileRequest;
import com.taskflow.user.dto.UserProfileDto;
import com.taskflow.user.model.UserProfile;
import com.taskflow.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository repository;

    @Transactional
    public UserProfileDto createProfile(CreateProfileRequest request) {
        if (repository.existsById(request.userId())) {
            throw new IllegalArgumentException("Profile already exists for user: " + request.userId());
        }

        UserProfile profile = UserProfile.builder()
                .id(request.userId())
                .name(request.name())
                .email(request.email())
                .build();

        return toDto(repository.save(profile));
    }

    public UserProfileDto getById(UUID id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

    public List<UserProfileDto> listActive() {
        return repository.findAllByActiveTrue().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public UserProfileDto update(UUID id, UpdateProfileRequest request) {
        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));

        profile.setName(request.name());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setJobTitle(request.jobTitle());
        profile.setDepartment(request.department());

        return toDto(repository.save(profile));
    }

    // Used by notification-service and task-service to verify a user exists before operations
    public boolean exists(UUID id) {
        return repository.existsById(id);
    }

    private UserProfileDto toDto(UserProfile p) {
        return new UserProfileDto(
                p.getId(), p.getName(), p.getEmail(),
                p.getAvatarUrl(), p.getJobTitle(), p.getDepartment(), p.isActive()
        );
    }
}
