package com.amusement.amusement_park.dto;

import lombok.Data;

@Data
public class UserProfile {
    private String email;
    private String role;
    private boolean verified;
    private String name;
    private String phoneNumber;
}
