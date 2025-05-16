package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.event.services.BudgetService;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetRepository {

    private final BudgetService budgetService;
    @Inject
    public BudgetRepository(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    public LiveData<Result<Budget>> getBudget(Long eventId) {
        MutableLiveData<Result<Budget>> result = new MutableLiveData<>();
        budgetService.getBudget(eventId).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<Product>> purchaseProduct(Long eventId, BudgetItem item) {
        MutableLiveData<Result<Product>> result = new MutableLiveData<>();
        budgetService.purchaseProduct(eventId, item).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<List<BudgetItem>>> getPurchased(Long eventId) {
        MutableLiveData<Result<List<BudgetItem>>> result = new MutableLiveData<>();
        budgetService.getPurchased(eventId).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<List<SolutionReview>>> getBudgetItems() {
        MutableLiveData<Result<List<SolutionReview>>> result = new MutableLiveData<>();
        budgetService.getBudgetItems().enqueue(handleResponse(result));
        return result;
    }
}
