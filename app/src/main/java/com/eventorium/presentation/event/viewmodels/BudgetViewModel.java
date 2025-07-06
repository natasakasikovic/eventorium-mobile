package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.budget.Budget;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.models.budget.BudgetSuggestion;
import com.eventorium.data.event.models.budget.UpdateBudgetItem;
import com.eventorium.data.event.repositories.BudgetRepository;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.solution.models.product.Product;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BudgetViewModel extends ViewModel {
    private final BudgetRepository budgetRepository;

    @Inject
    public BudgetViewModel(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public LiveData<Result<List<BudgetSuggestion>>> getBudgetSuggestions(Long eventId, Long categoryId, double price) {
        return budgetRepository.getBudgetSuggestions(eventId, categoryId, price);
    }

    public LiveData<Result<BudgetItem>> createBudgetItem(Long eventId, BudgetItemRequest request) {
        return budgetRepository.createBudgetItem(eventId, request);
    }

    public LiveData<Result<Void>> deleteBudgetItem(Long eventId, Long itemId) {
        return budgetRepository.deleteBudgetItem(eventId, itemId);
    }

    public LiveData<Result<Void>> updateActiveCategories(Long eventId, List<Category> categories) {
        return budgetRepository.updateActiveCategories(eventId, categories);
    }

    public LiveData<Result<BudgetItem>> updateBudgeItem(Long eventId, Long itemId, UpdateBudgetItem request) {
        return budgetRepository.updateBudgeItem(eventId, itemId, request);
    }


    public LiveData<Result<Product>> purchaseProduct(Long eventId, BudgetItemRequest product) {
        return budgetRepository.purchaseProduct(eventId, product);
    }

    public LiveData<Result<Budget>> getBudget(Long eventId) {
        return budgetRepository.getBudget(eventId);
    }
    public LiveData<Result<List<SolutionReview>>> getAllBudgetItems() {
        return budgetRepository.getAllBudgetItems();
    }

    public LiveData<Result<List<BudgetItem>>> getBudgetItems(Long eventId) {
        return budgetRepository.getBudgetItems(eventId);
    }
}
