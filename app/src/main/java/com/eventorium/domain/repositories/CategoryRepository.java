package com.eventorium.domain.repositories;

import androidx.lifecycle.LiveData;

import com.eventorium.data.models.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryRepository {
    LiveData<List<Category>> getCategories();
}
