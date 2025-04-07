package com.eventorium.data.interaction.models.rating;

import com.eventorium.data.auth.models.UserDetails;

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
public class Rating {
    private Long id;
    private Integer rating;
    private UserDetails user;

    public Rating(Integer rating) {
        this.rating = rating;
    }
}
