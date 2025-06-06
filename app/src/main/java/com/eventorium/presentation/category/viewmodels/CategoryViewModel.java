package com.eventorium.presentation.category.viewmodels;

import static java.util.stream.Collectors.toList;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.shared.models.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CategoryViewModel extends ViewModel {

    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final CategoryRepository categoryRepository;

    @Inject
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

    public LiveData<Result<Void>> deleteCategory(Long id) {
        return categoryRepository.deleteCategory(id);
    }

    public void removeCategory(Long id) {
        categories.postValue(Objects.requireNonNull(categories.getValue())
                .stream()
                .filter(category -> !Objects.equals(category.getId(), id))
                .collect(toList()));
    }

    public LiveData<Result<Category>> createCategory(CategoryRequest category) {
        return categoryRepository.createCategory(category);
    }

    public LiveData<Result<Category>> updateCategory(Long id, CategoryRequest category) {
        return categoryRepository.updateCategory(id, category);
    }

    public List<String> getExistingCategoriesName() {
        List<String> result = new ArrayList<>();
        result.add("");
        List<String> categoriesNames;
        categoriesNames = Objects.requireNonNull(categories.getValue())
                .stream()
                .map(Category::getName)
                .collect(toList());
        result.addAll(categoriesNames);
        return result;
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
