package com.eventorium.data.event.models.event;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
