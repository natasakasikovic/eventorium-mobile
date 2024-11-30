package com.eventorium.data.mappers;

import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryRequestDto;
import com.eventorium.data.models.CategoryResponseDto;

public class CategoryMapper {

    public static Category fromResponse(CategoryResponseDto responseDto) {
        return new Category(responseDto.getId(), responseDto.getName(), responseDto.getDescription());
    }

    public static CategoryRequestDto toRequest(Category category) {
        return new CategoryRequestDto(category.getName(), category.getDescription());
    }
}
