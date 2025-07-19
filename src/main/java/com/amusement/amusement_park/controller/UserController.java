package com.amusement.amusement_park.controller;

import com.amusement.amusement_park.entity.User;
import com.amusement.amusement_park.repository.UserRepository;
import com.amusement.amusement_park.service.OtpGenerator;
import com.amusement.amusement_park.service.OtpStore;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import com.amusement.amusement_park.util.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final OtpStore otpStore;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role");

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setVerified(false);

        userRepository.save(user);

        String otp = OtpGenerator.generateOtp();
        otpStore.storeOtp(email, otp);
        logger.info("OTP for {} is: {}", email, otp); // replace with email service later

        return ResponseEntity.ok("Registered successfully. OTP sent to console (valid for 5 min).");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        return userRepository.findByEmail(email)
                .map(user -> {
                    String storedOtp = otpStore.getOtp(email);
                    if (storedOtp == null) {
                        return ResponseEntity.badRequest().body("OTP expired or not found.");
                    }
                    if (!storedOtp.equals(otp)) {
                        return ResponseEntity.badRequest().body("Invalid OTP.");
                    }
                    user.setVerified(true);
                    userRepository.save(user);
                    otpStore.clearOtp(email);
                    return ResponseEntity.ok("OTP verified. Account activated.");
                })
                .orElse(ResponseEntity.badRequest().body("User not found."));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = optionalUser.get();
        if (user.isVerified()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is already verified.");
        }

        String otp = OtpGenerator.generateOtp();
        otpStore.storeOtp(email, otp);

        logger.info("Resent OTP for {} is: {}", email, otp);

        return ResponseEntity.ok("OTP resent to your email (console for now).");
    }

}
