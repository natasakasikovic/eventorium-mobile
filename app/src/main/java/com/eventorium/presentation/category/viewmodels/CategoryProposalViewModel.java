package com.eventorium.presentation.category.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryUpdateStatusDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.repositories.CategoryProposalRepository;
import com.eventorium.data.category.repositories.CategoryRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@Getter
@HiltViewModel
public class CategoryProposalViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categoryProposals = new MutableLiveData<>();
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

    public void refreshProposals(Long id) {
        categoryProposals.postValue(Objects.requireNonNull(categoryProposals.getValue())
                .stream()
                .filter(category -> !Objects.equals(category.getId(), id))
                .collect(toList()));
    }

    public LiveData<Boolean> updateCategoryStatus(Long id, CategoryUpdateStatusDto dto) {
        return categoryProposalRepository.updateCategoryStatus(id, dto);
    }

    public LiveData<Boolean> updateCategoryProposal(Long id, CategoryRequestDto dto) {
        return categoryProposalRepository.updateCategoryProposal(id, dto);
    }

    public LiveData<Boolean> changeCategory(Long id, CategoryRequestDto dto) {
        return categoryProposalRepository.changeCategory(id, dto);
    }
}
