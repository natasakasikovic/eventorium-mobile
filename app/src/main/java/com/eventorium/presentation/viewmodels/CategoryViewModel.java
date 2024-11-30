package com.eventorium.presentation.viewmodels;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.models.Category;
import com.eventorium.domain.repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private final CategoryRepository categoryRepository;

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
        categoryRepository.getCategories().observeForever(categories::postValue);
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        categoryRepository.getCategories().removeObserver(categories::postValue);
    }
}
