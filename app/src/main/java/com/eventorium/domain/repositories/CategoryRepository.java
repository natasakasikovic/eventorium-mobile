package com.eventorium.domain.repositories;

import androidx.lifecycle.LiveData;

import com.eventorium.data.models.Category;
import com.eventorium.data.dtos.categories.CategoryRequestDto;
import com.eventorium.data.dtos.categories.CategoryResponseDto;

import java.util.List;

public interface CategoryRepository {
    LiveData<List<Category>> getCategories();
    LiveData<Category> updateCategory(Long id, CategoryRequestDto category);
    LiveData<Category> createCategory(CategoryRequestDto category);
    LiveData<Boolean> deleteCategory(Long id);
}
