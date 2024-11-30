package com.eventorium.presentation.viewmodels;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.mappers.CategoryMapper;
import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryResponseDto;
import com.eventorium.data.util.RetrofitApi;
import com.eventorium.domain.repositories.CategoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private final CategoryRepository categoryRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CategoryViewModel(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        fetchCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void updateCategory(Category updateCategory) {

    }

    private void fetchCategories() {
        executor.execute(() -> {
            try {
                List<Category> result = categoryRepository.getCategories();
                categories.postValue(result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }
}
