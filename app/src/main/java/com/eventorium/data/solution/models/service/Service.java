package com.eventorium.data.solution.models.service;

import com.eventorium.data.auth.models.ChatUserDetails;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.company.models.CompanyDetails;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.util.models.ReservationType;
import com.eventorium.data.util.models.Status;

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
public class Service {
    private Long id;
    private String name;
    private String description;
    private String specialties;
    private Double price;
    private Double discount;
    private Status status;
    private List<EventType> eventTypes;
    private Double rating;
    private Category category;
    private ReservationType type;
    private LocalDate reservationDeadline;
    private LocalDate cancellationDeadline;
    private Integer minDuration;
    private Integer maxDuration;
    private Boolean available;
    private ChatUserDetails provider;
    private CompanyDetails company;
    private Boolean visible;
}
