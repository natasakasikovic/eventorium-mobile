package com.eventorium.data.solution.dtos;

import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.event.dtos.EventTypeResponseDto;
import com.eventorium.data.util.models.ReservationType;

import java.time.LocalDate;
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
public class CreateServiceRequestDto {
    private String name;
    private String description;
    private String specialties;
    private double price;
    private double discount;
    private List<EventTypeResponseDto> eventTypes;
    private CategoryResponseDto category;
    private ReservationType type;
    private LocalDate reservationDeadline;
    private LocalDate cancellationDeadline;
    private Integer minDuration;
    private Integer maxDuration;
}
