package com.eventorium.data.solution.models.product;

import com.eventorium.data.auth.models.ChatUserDetails;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.company.models.CompanyDetails;
import com.eventorium.data.event.models.eventtype.EventType;
import com.eventorium.data.shared.models.Status;

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
public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Status status;
    private Boolean available;
    private Boolean visible;
    private List<EventType> eventTypes;
    private Category category;
    private ChatUserDetails provider;
    private CompanyDetails company;
    private Double rating;
}
