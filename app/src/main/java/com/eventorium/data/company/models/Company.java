package com.eventorium.data.company.models;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.shared.models.City;

import java.time.LocalTime;

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
    private LocalTime openingHours;
    private LocalTime closingHours;
    private User provider;
}
