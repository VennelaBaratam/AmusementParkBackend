package com.amusement.amusement_park.controller;

import com.amusement.amusement_park.entity.MembershipPlan;
import com.amusement.amusement_park.repository.MembershipPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanRepository membershipPlanRepository;

    @PostMapping("/admin/membership-plans")
    public ResponseEntity<String> addPlan(@RequestBody MembershipPlan plan) {
        membershipPlanRepository.save(plan);
        return ResponseEntity.ok("Membership plan added successfully.");
    }

    @GetMapping("/membership-plans")
    public List<MembershipPlan> getAllPlans() {
        return membershipPlanRepository.findAll();
    }
}
