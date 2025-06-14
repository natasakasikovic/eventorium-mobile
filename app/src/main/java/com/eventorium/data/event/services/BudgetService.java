package com.eventorium.data.event.services;

import com.eventorium.data.event.models.BudgetSuggestion;
import com.eventorium.data.event.models.budget.Budget;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.solution.models.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BudgetService {

    @GET("events/{event-id}/budget/suggestions")
    Call<List<BudgetSuggestion>> getBudgetSuggestions(
            @Path("event-id") Long eventId,
            @Query("category-id") Long categoryId,
            @Query("price") double price
    );

    @GET("events/{event-id}/budget")
    Call<Budget> getBudget(@Path("event-id") Long eventId);

    @GET("events/{event-id}/budget/budget-items")
    Call<List<BudgetItem>> getBudgetItems(@Path("event-id") Long eventId);

    @GET("budget-items")
    Call<List<SolutionReview>> getAllBudgetItems();

    @POST("events/{event-id}/budget/purchase")
    Call<Product> purchaseProduct(@Path("event-id") Long eventId, @Body BudgetItemRequest item);

}
