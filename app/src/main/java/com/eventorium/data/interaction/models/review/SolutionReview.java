package com.eventorium.data.interaction.models.review;

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
public class SolutionReview {
    private Long id;
    private String name;
    private SolutionType solutionType;
}
