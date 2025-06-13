package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.BudgetSuggestion;
import com.eventorium.data.event.models.budget.Budget;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.services.BudgetService;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.shared.models.Result;

import java.util.List;

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
}
