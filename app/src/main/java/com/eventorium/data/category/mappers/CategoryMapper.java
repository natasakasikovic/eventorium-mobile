package com.eventorium.data.category.mappers;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryResponseDto;

public class CategoryMapper {

    public static Category fromResponse(CategoryResponseDto responseDto) {
        return new Category(responseDto.getId(), responseDto.getName(), responseDto.getDescription());
    }

    public static CategoryRequestDto toRequest(Category category) {
        return new CategoryRequestDto(category.getName(), category.getDescription());
    }

    public static CategoryResponseDto toResponse(Category c) {
        return new CategoryResponseDto(c.getId(), c.getName(), c.getDescription());
    }
}
