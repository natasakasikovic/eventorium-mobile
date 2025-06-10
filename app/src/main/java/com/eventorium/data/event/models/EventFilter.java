package com.eventorium.data.event.models;

import java.time.LocalDate;

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
public class EventFilter {
    private String name;
    private String description;
    private String type;
    private Integer maxParticipants;
    private String city;
    private LocalDate from;
    private LocalDate to;
}