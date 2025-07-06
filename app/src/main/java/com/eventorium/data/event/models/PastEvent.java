package com.eventorium.data.event.models;

import com.eventorium.data.shared.models.City;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PastEvent {
    private Long id;
    private String name;
    private LocalDate date;
    private String privacy;
    private Integer maxParticipants;
    private City city;
}
