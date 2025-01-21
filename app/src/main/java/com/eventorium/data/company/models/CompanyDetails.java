package com.eventorium.data.company.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetails {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String openingHours;
    private String closingHours;
}