package com.eventorium.data.mappers;

import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryResponseDto;

public class CategoryMapper {

    public static Category fromResponse(CategoryResponseDto responseDto) {
        return new Category(responseDto.getId(), responseDto.getName(), responseDto.getDescription());
    }
}
