package com.eventorium.data.solution.models.product;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;

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
public class CreateProduct {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<EventType> eventTypes;
    private Category category;
    private boolean isVisible;
    private boolean isAvailable;
}
