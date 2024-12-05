package com.eventorium.presentation.category.viewmodels;

import static java.util.stream.Collectors.toList;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.models.Category;
import com.eventorium.data.dtos.categories.CategoryRequestDto;
import com.eventorium.data.repositories.CategoryRepository;

import java.util.List;
import java.util.Objects;

public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
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

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> deleteCategory(Long id) {
        LiveData<Boolean> result =  categoryRepository.deleteCategory(id);
        if(result != null && Boolean.TRUE.equals(result.getValue())) {
            categories.postValue(Objects.requireNonNull(categories.getValue())
                    .stream()
                    .filter(category -> !Objects.equals(category.getId(), id))
                    .collect(toList()));
        }
        return result;
    }

    public LiveData<Category> createCategory(CategoryRequestDto category) {
        return categoryRepository.createCategory(category);
    }

    public LiveData<Category> updateCategory(Long id, CategoryRequestDto category) {
        return categoryRepository.updateCategory(id, category);
    }

    private void fetchCategories() {
        isLoading.setValue(true);
        categoryRepository.getCategories().observeForever(categories -> {
            this.categories.postValue(categories);
            isLoading.postValue(false);
        });
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
