package com.eventorium.data.event.models;

import com.eventorium.data.category.models.Category;

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
    private Double plannedAmount;
    private Long itemId;
    private Category category;
}
