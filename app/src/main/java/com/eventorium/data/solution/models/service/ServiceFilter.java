package com.eventorium.data.solution.models.service;

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
public class ServiceFilter {
    private String name;
    private String description;
    private String type;
    private String category;
    private Boolean availability;
    private Double minPrice;
    private Double maxPrice;
}