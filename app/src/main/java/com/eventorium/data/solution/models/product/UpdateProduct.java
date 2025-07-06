package com.eventorium.data.solution.models.product;

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
public class UpdateProduct {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<Long> eventTypesIds;
    private Boolean available;
    private Boolean visible;
}
