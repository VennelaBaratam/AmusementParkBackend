package com.amusement.amusement_park.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int durationInDays;
}