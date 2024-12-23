package com.eventorium.data.solution.models;

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
public class PriceListItem {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private Double netPrice;
}
