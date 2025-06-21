package com.eventorium.data.event.services;

import com.eventorium.data.event.models.budget.BudgetSuggestion;
import com.eventorium.data.event.models.budget.Budget;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.models.budget.UpdateBudgetItem;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.solution.models.product.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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

    @POST("events/{event-id}/budget/budget-item")
    Call<BudgetItem> createBudgetItem(@Path("event-id") Long eventId, @Body BudgetItemRequest request);

    @POST("events/{event-id}/budget/purchase")
    Call<Product> purchaseProduct(@Path("event-id") Long eventId, @Body BudgetItemRequest item);

    @PATCH("/events/{event-id}/budget/active-categories")
    Call<ResponseBody> updateActiveCategories(@Path("event-id") Long eventId, @Body List<Long> categoryIds);

    @PATCH("/events/{event-id}/budget/budget-items/{item-id}")
    Call<BudgetItem> updateBudgetItemPlannedAmount(
            @Path("event-id") Long eventId,
            @Path("item-id") Long itemId,
            @Body UpdateBudgetItem request
    );

    @DELETE("events/{event-id}/budget/budget-item/{item-id}")
    Call<ResponseBody> deleteBudgeItem(@Path("event-id") Long eventId, @Path("item-id") Long itemId);
}
