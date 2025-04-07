package com.eventorium.data.interaction.models.review;


import android.graphics.Bitmap;

import com.eventorium.data.interaction.models.rating.Rating;

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
    private Double price;
    private Double discount;
    private Rating rating;
    private ReviewType type;
    private Bitmap image;
}
