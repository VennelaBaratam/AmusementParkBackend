package com.amusement.amusement_park.controller;

import com.amusement.amusement_park.entity.User;
import com.amusement.amusement_park.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.amusement.amusement_park.dto.UserProfile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;

    @GetMapping("/{email}")
    public ResponseEntity<UserProfile> getProfileByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email).map(user -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setEmail(user.getEmail());
            userProfile.setName(user.getName());
            userProfile.setPhoneNumber(user.getPhoneNumber());
            userProfile.setRole(user.getRole());
            userProfile.setVerified(user.isVerified());
            return ResponseEntity.ok(userProfile);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<String> updateProfileByEmail(
            @PathVariable String email,
            @RequestBody User updatedUser) {

        return userRepository.findByEmail(email).map(user -> {
            user.setName(updatedUser.getName() != null ? updatedUser.getName() : user.getName());
            user.setPhoneNumber(
                    updatedUser.getPhoneNumber() != null ? updatedUser.getPhoneNumber() : user.getPhoneNumber());
            userRepository.save(user);
            return ResponseEntity.ok("Profile updated successfully.");
        }).orElse(ResponseEntity.badRequest().body("User not found."));
    }

}
