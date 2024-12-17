package com.eventorium.data.solution.dtos;

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
public class ServiceFilterDto {
    private String category;
    private String eventType;
    private Boolean availability;
    private Double minPrice;
    private Double maxPrice;
}
