package com.eventorium.data.auth.models;

import com.eventorium.data.shared.models.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {
    private Long id;
    private String email;
    private String name;
    private String lastname;
    private String phoneNumber;
    private String address;
    private City city;
    private String role;
}
