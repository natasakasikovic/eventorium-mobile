package com.eventorium.data.event.models.budget;

import com.eventorium.data.category.models.Category;

import java.util.List;

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
public class Budget {
    private Double plannedAmount;
    private Double spentAmount;
    private List<Category> activeCategories;
}
