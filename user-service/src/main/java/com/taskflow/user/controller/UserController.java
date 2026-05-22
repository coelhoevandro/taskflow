package com.taskflow.user.controller;

import com.taskflow.user.dto.CreateProfileRequest;
import com.taskflow.user.dto.UpdateProfileRequest;
import com.taskflow.user.dto.UserProfileDto;
import com.taskflow.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/api/users")
    public List<UserProfileDto> listUsers() {
        return userProfileService.listActive();
    }

    @GetMapping("/api/users/{id}")
    public UserProfileDto getUser(@PathVariable UUID id) {
        return userProfileService.getById(id);
    }

    @PutMapping("/api/users/{id}")
    public UserProfileDto updateUser(@PathVariable UUID id,
                                     @Valid @RequestBody UpdateProfileRequest request) {
        return userProfileService.update(id, request);
    }

    // Internal endpoint — called by task-service and notification-service
    @GetMapping("/api/internal/users/{id}/exists")
    public ResponseEntity<Map<String, Boolean>> exists(@PathVariable UUID id) {
        return ResponseEntity.ok(Map.of("exists", userProfileService.exists(id)));
    }

    // Internal endpoint — called when a user registers through auth-service
    @PostMapping("/api/internal/users")
    public ResponseEntity<UserProfileDto> createProfile(@Valid @RequestBody CreateProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createProfile(request));
    }
}
