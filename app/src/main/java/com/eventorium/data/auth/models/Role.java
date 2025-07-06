package com.eventorium.data.auth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private String name;

    @Override
    public String toString() {
        String[] parts = this.name.split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String part : parts) {
            formattedName.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }

        return formattedName.toString().trim();
    }
}
