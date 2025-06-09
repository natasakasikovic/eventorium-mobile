package com.eventorium.data.interaction.models.comment;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.review.ReviewType;

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
public class Comment {
    private Long id;
    private String comment;
    private LocalDateTime creationDate;
    private UserDetails user;
    private ReviewType type;
    private String displayName;
    private Long objectId;
}
