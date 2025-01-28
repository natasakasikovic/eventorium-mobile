package com.eventorium.data.company.models;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.shared.models.City;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private Long id;
    private String name;
    private String address;
    private City city;
    private String phoneNumber;
    private String description;
    private String email;
    private String openingHours;
    private String closingHours;
    private User provider;
}
