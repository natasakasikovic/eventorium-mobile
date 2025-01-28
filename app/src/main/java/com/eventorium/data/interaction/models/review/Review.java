package com.eventorium.data.interaction.models.review;

import com.eventorium.data.auth.models.UserDetails;

import java.time.LocalDateTime;

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
public class Review {
    private Long id;
    private LocalDateTime creationDate;
    private Integer rating;
    private String feedback;
    private UserDetails user;
}
