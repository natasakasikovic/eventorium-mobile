package com.eventorium.domain.repositories;

import com.eventorium.data.models.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories() throws IOException;
}
