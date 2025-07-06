package com.eventorium.data.event.models.event;

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
public class Activity {
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
}
