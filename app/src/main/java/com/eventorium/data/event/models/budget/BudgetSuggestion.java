package com.eventorium.data.event.models.budget;

import android.graphics.Bitmap;

import com.eventorium.data.solution.models.SolutionType;

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
public class BudgetSuggestion {
    private Long id;
    private SolutionType solutionType;
    private String name;
    private Double price;
    private Double discount;
    private Double rating;
    private Bitmap image;
}
