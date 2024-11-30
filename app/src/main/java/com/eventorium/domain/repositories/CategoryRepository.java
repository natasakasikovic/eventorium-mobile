package com.eventorium.domain.repositories;

import androidx.lifecycle.LiveData;

import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryRequestDto;
import com.eventorium.data.models.CategoryResponseDto;

import java.io.IOException;
import java.util.List;

public interface CategoryRepository {
    LiveData<List<Category>> getCategories();
    LiveData<Category> updateCategory(Long id, Category category);
}
