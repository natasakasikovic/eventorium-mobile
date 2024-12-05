package com.eventorium.data.mappers;

import com.eventorium.data.dtos.eventtypes.EventTypeResponseDto;
import com.eventorium.data.models.Category;
import com.eventorium.data.models.EventType;

import java.util.List;
import java.util.stream.Collectors;

public class EventTypeMapper {

    public static EventType fromResponse(EventTypeResponseDto responseDto) {
        List<Category> categories = responseDto.getSuggestedCategories().stream()
                .map(CategoryMapper::fromResponse)
                .collect(Collectors.toList());
        return new EventType(responseDto.getId(), responseDto.getName(), responseDto.getDescription(), categories);
    }
}
