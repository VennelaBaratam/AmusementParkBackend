package com.amusement.amusement_park.dto;

//import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    private String name;
    // @JsonProperty("phone_number")
    private String phoneNumber;
}
