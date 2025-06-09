package com.eventorium.data.interaction.models.comment;

import com.eventorium.data.interaction.models.review.ReviewType;

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
public class CreateComment {
    private String comment;
    private ReviewType type;
    private Long objectId;
}
