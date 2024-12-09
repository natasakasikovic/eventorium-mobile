package com.eventorium.data.event.mappers;

import com.eventorium.data.category.mappers.CategoryMapper;
import com.eventorium.data.event.dtos.EventTypeResponseDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;

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
