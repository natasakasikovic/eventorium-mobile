package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.budget.Budget;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.models.budget.BudgetSuggestion;
import com.eventorium.data.event.models.budget.UpdateBudgetItem;
import com.eventorium.data.event.services.BudgetService;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.solution.models.product.Product;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class BudgetRepository {

    private final BudgetService budgetService;
    @Inject
    public BudgetRepository(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public LiveData<Result<Budget>> getBudget(Long eventId) {
        MutableLiveData<Result<Budget>> result = new MutableLiveData<>();
        budgetService.getBudget(eventId).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<BudgetItem>>> getBudgetItems(Long eventId) {
        MutableLiveData<Result<List<BudgetItem>>> result = new MutableLiveData<>();
        budgetService.getBudgetItems(eventId).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Product>> purchaseProduct(Long eventId, BudgetItemRequest item) {
        MutableLiveData<Result<Product>> result = new MutableLiveData<>();
        budgetService.purchaseProduct(eventId, item).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<BudgetItem>> createBudgetItem(Long eventId, BudgetItemRequest request) {
        MutableLiveData<Result<BudgetItem>> result = new MutableLiveData<>();
        budgetService.createBudgetItem(eventId, request).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> deleteBudgetItem(Long eventId, Long itemId) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        budgetService.deleteBudgeItem(eventId, itemId).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<BudgetItem>> updateBudgeItem(Long eventId, Long itemId, UpdateBudgetItem request) {
        MutableLiveData<Result<BudgetItem>> result = new MutableLiveData<>();
        budgetService.updateBudgetItemPlannedAmount(eventId, itemId, request).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> updateActiveCategories(Long eventId, List<Category> categories) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        budgetService.updateActiveCategories(eventId, getCategoryIds(categories)).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<List<BudgetSuggestion>>> getBudgetSuggestions(Long eventId, Long categoryId, double price) {
        MutableLiveData<Result<List<BudgetSuggestion>>> result = new MutableLiveData<>();
        budgetService.getBudgetSuggestions(eventId, categoryId, price).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<SolutionReview>>> getAllBudgetItems() {
        MutableLiveData<Result<List<SolutionReview>>> result = new MutableLiveData<>();
        budgetService.getAllBudgetItems().enqueue(handleGeneralResponse(result));
        return result;
    }

    private List<Long> getCategoryIds(List<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toList());
    }
}
