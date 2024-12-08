package com.eventorium.data.event.mappers;

import static java.util.stream.Collectors.toList;

import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.category.mappers.CategoryMapper;
import com.eventorium.data.event.dtos.EventTypeRequestDto;
import com.eventorium.data.event.dtos.EventTypeResponseDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.EventType;

import java.util.List;
import java.util.stream.Collectors;

public class EventTypeMapper {

    public static EventType fromResponse(EventTypeResponseDto responseDto) {
        List<Category> categories = responseDto.getSuggestedCategories().stream()
                .map(CategoryMapper::fromResponse)
                .collect(toList());
        return new EventType(responseDto.getId(), responseDto.getName(), responseDto.getDescription(), categories);
    }

    public static EventTypeResponseDto toRequest(EventType eventType) {
        List<CategoryResponseDto> categories = eventType.getSuggestedCategories().stream()
                .map(CategoryMapper::toResponse)
                .collect(toList());

        return EventTypeResponseDto.builder()
                .id(eventType.getId())
                .name(eventType.getName())
                .description(eventType.getDescription())
                .suggestedCategories(categories)
                .build();
    }
}
