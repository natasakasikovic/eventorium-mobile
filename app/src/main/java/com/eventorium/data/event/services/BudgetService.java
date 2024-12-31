package com.eventorium.data.event.services;

import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.solution.models.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BudgetService {

    @GET("events/{event-id}/budget")
    Call<Budget> getBudget(@Path("event-id") Long eventId);

    @GET("events/{event-id}/budget/purchased")
    Call<List<BudgetItem>> getPurchased(@Path("event-id") Long eventId);

    @POST("events/{event-id}/budget/purchase")
    Call<Product> purchaseProduct(@Path("event-id") Long eventId, @Body BudgetItem item);

}
