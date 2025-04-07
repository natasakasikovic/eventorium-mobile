package com.eventorium.data.event.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.event.services.BudgetService;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
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

    public LiveData<Result<List<ProductSummary>>> getPurchased(Long eventId) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        budgetService.getPurchased(eventId).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<List<SolutionReview>>> getBudgetItems() {
        MutableLiveData<Result<List<SolutionReview>>> result = new MutableLiveData<>();
        budgetService.getBudgetItems().enqueue(handleResponse(result));
        return result;
    }

    private<T> Callback<T> handleResponse(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success((response.body())));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        };
    }
}
