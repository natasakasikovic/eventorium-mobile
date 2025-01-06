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
    private String category;
    private String eventType;
    private Boolean availability;
    private Double minPrice;
    private Double maxPrice;
}
