package com.eventorium.data.solution.models.service;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class Reservation {
    private Long id;
    private Long eventId;
    private String eventName;
    private Long serviceId;
    private String serviceName;
    private LocalDate date;
    private String startingTime;
    private String endingTime;
}
