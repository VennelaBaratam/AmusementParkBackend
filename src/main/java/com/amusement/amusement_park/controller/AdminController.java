package com.amusement.amusement_park.controller;

import com.amusement.amusement_park.entity.User;
import com.amusement.amusement_park.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.amusement.amusement_park.dto.UserProfile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /*
     * @GetMapping("/user/{id}")
     * public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
     * return userRepository.findById(id)
     * .map(user -> {
     * UserProfile dto = new UserProfile();
     * dto.setEmail(user.getEmail());
     * dto.setRole(user.getRole());
     * dto.setVerified(user.isVerified());
     * dto.setName(user.getName());
     * dto.setPhoneNumber(user.getPhoneNumber());
     * return ResponseEntity.ok(dto);
     * })
     * .orElse(ResponseEntity.notFound().build());
     * }
     */

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserRolePhoneAndName(

            @PathVariable Long id,

            @RequestParam(required = false) String role,

            @RequestParam(required = false) String phoneNumber,

            @RequestParam(required = false) String name) {

        return userRepository.findById(id).map(user -> {
            if (role != null && !role.trim().isEmpty()) {
                user.setRole(role.toUpperCase());
            }
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                user.setPhoneNumber(phoneNumber);
            }
            if (name != null && !name.trim().isEmpty()) {
                user.setName(name);
            }
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully (role/phone/name).");
        }).orElse(ResponseEntity.badRequest().body("User not found with id: " + id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }
}
