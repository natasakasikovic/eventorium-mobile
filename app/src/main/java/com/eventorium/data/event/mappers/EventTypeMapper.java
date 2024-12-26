package com.eventorium.data.event.mappers;

import static java.util.stream.Collectors.toList;
import com.eventorium.data.event.dtos.EventTypeResponseDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;

import java.util.List;

public class EventTypeMapper {

    public static EventType fromResponse(EventTypeResponseDto responseDto) {
        List<Category> categories = responseDto.getSuggestedCategories();
        return new EventType(responseDto.getId(), responseDto.getName(), responseDto.getDescription(), categories);
    }

    public static EventTypeResponseDto toRequest(EventType eventType) {
        List<Category> categories = eventType.getSuggestedCategories();

        return EventTypeResponseDto.builder()
                .id(eventType.getId())
                .name(eventType.getName())
                .description(eventType.getDescription())
                .suggestedCategories(categories)
                .build();
    }
}
