package com.amusement.amusement_park.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class UserMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private MembershipPlan plan;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // ACTIVE, CANCELLED, EXPIRED
}