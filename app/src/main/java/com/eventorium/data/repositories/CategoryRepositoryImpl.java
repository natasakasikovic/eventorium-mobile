package com.eventorium.data.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import com.eventorium.data.mappers.CategoryMapper;
import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryResponseDto;
import com.eventorium.data.services.CategoryService;
import com.eventorium.domain.repositories.CategoryRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryService categoryService;

    public CategoryRepositoryImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public List<Category> getCategories() throws IOException {
        List<CategoryResponseDto> dtos = categoryService.getCategories().execute().body();

        Log.i("IS_NULL", String.valueOf(dtos == null));

        if (dtos != null) {
            return dtos.stream()
                    .map(CategoryMapper::fromResponse)
                    .collect(toList());
        }
        return Collections.emptyList();
    }
}
