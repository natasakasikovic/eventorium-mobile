package com.eventorium.data.event.models.budget;

import com.eventorium.data.category.models.Category;
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
public class BudgetItemRequest {
    private Double plannedAmount;
    private Long itemId;
    private Category category;
    private SolutionType type;
}
