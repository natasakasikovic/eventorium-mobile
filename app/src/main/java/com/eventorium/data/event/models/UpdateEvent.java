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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEvent {
    private String name;
    private String description;
    private LocalDate date;
    private Integer maxParticipants;
    private EventType eventType;
    private City city;
    private String address;
}
