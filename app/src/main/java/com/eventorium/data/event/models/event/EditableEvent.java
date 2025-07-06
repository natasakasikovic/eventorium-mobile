package com.eventorium.data.event.models.event;

import com.eventorium.data.event.models.eventtype.EventType;
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
public class EditableEvent {
    private String name;
    private String description;
    private LocalDate date;
    private Integer maxParticipants;
    private EventType type;
    private City city;
    private String address;
}
