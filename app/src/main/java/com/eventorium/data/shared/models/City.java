package com.eventorium.data.shared.models;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Long id;
    private String name;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
