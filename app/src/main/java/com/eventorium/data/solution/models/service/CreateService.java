package com.eventorium.data.solution.models.service;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.eventtype.EventType;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateService {
    private String name;
    private String description;
    private String specialties;
    private double price;
    private double discount;
    private List<EventType> eventTypes;
    private Category category;
    private ReservationType type;
    private Integer reservationDeadline;
    private Integer cancellationDeadline;
    private Integer minDuration;
    private Integer maxDuration;
    private Boolean isAvailable;
    private Boolean isVisible;
}
