package com.eventorium.presentation.category.viewmodels;

import static java.util.stream.Collectors.toList;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.repositories.CategoryProposalRepository;
import com.eventorium.data.category.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@Getter
@HiltViewModel
public class CategoryProposalViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categoryProposals = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> existingCategories = new MutableLiveData<>();
    private final MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final CategoryProposalRepository categoryProposalRepository;
    private final CategoryRepository categoryRepository;

    @Inject
    public CategoryProposalViewModel(
            CategoryRepository categoryRepository,
            CategoryProposalRepository categoryProposalRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryProposalRepository = categoryProposalRepository;
        fetchCategoryProposals();
    }

    public void fetchCategoryProposals() {
        isLoading.setValue(true);
        categoryProposalRepository.getCategoryProposals().observeForever(categories -> {
            this.categoryProposals.postValue(categories);
            isLoading.postValue(false);
        });
    }

    public void setCategoryProposals(List<Category> categoriesProposals) {
        this.categoryProposals.setValue(categoriesProposals);
    }

    public void setExistingCategories(List<Category> categories) {
        this.categoryProposals.setValue(categories);
    }

    public void setSelectedCategory(Category category) {
        selectedCategory.setValue(category);
    }

    public void updateCategory(Category updateCategory) {

    }

    public List<String> getExistingCategoriesName() {
        List<String> result = new ArrayList<>();
        result.add("");
        List<String> categoriesNames;
        categoriesNames = Objects.requireNonNull(existingCategories.getValue())
                .stream()
                .map(Category::getName)
                .collect(toList());
        result.addAll(categoriesNames);
        return result;
    }
}
