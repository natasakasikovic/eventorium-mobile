package com.eventorium.data.auth.models;

import com.eventorium.data.shared.models.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private String lastname;
    private String address;
    private String phoneNumber;
    private City city;
}
