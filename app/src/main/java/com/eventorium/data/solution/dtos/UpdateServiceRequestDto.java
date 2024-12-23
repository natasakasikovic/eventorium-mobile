package com.eventorium.data.solution.dtos;

import com.eventorium.data.util.models.ReservationType;

import java.time.LocalDate;
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
public class UpdateServiceRequestDto {
    private String name;
    private String description;
    private String specialties;
    private Double price;
    private Double discount;
    private List<Long> eventTypesIds;
    private ReservationType type;
    private LocalDate reservationDeadline;
    private LocalDate cancellationDeadline;
    private Integer minDuration;
    private Integer maxDuration;
    private Boolean available;
    private Boolean visible;
}
