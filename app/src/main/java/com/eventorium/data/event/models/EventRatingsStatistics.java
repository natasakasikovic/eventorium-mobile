package com.eventorium.data.event.models;

import java.util.Map;

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
public class EventRatingsStatistics {
    private String eventName;
    private int totalVisitors;
    private int totalRatings;
    private Map<Integer, Integer> ratingsCount;
}
