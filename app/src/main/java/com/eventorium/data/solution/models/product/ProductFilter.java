package com.eventorium.data.solution.models.product;

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
public class ProductFilter {
    private String name;
    private String description;
    private String type;
    private String category;
    private Boolean availability;
    private Double minPrice;
    private Double maxPrice;
}