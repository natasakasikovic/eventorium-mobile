package com.eventorium.data.event.models.budget;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.solution.models.SolutionType;

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
public class BudgetItem {
    private Long id;
    private Double plannedAmount;
    private Double spentAmount;
    private LocalDateTime processedAt;
    private SolutionType type;
    private Long solutionId;
    private String solutionName;
    private BudgetItemStatus status;
    private Category category;
}
