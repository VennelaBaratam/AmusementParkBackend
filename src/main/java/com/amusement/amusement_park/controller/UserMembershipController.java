package com.amusement.amusement_park.controller;

import com.amusement.amusement_park.entity.MembershipPlan;
import com.amusement.amusement_park.entity.User;
import com.amusement.amusement_park.entity.UserMembership;
import com.amusement.amusement_park.repository.MembershipPlanRepository;
import com.amusement.amusement_park.repository.UserMembershipRepository;
import com.amusement.amusement_park.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-memberships")
@RequiredArgsConstructor
public class UserMembershipController {

    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final UserMembershipRepository userMembershipRepository;

    @PostMapping("/subscribe/{planId}")
    public ResponseEntity<String> subscribe(@PathVariable Long planId, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }
        User user = userOpt.get();

        Optional<MembershipPlan> planOpt = membershipPlanRepository.findById(planId);
        if (planOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Plan not found.");
        }
        MembershipPlan plan = planOpt.get();

        UserMembership membership = new UserMembership();
        membership.setUser(user);
        membership.setPlan(plan);
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusDays(plan.getDurationInDays()));
        membership.setStatus("ACTIVE");

        userMembershipRepository.save(membership);

        return ResponseEntity.ok("Successfully subscribed to " + plan.getName());
    }

    @GetMapping("/my")
    public ResponseEntity<?> viewMyMembership(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }
        User user = userOpt.get();

        Optional<UserMembership> membershipOpt = userMembershipRepository.findByUser(user);
        return membershipOpt
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok("No active membership."));
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelMembership(Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }
        User user = userOpt.get();

        Optional<UserMembership> membershipOpt = userMembershipRepository.findByUser(user);
        if (membershipOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No active membership to cancel.");
        }

        UserMembership membership = membershipOpt.get();
        membership.setStatus("CANCELLED");
        userMembershipRepository.save(membership);

        return ResponseEntity.ok("Membership cancelled successfully.");
    }
}
