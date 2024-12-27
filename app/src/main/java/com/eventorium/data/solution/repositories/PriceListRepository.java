package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.pricelist.UpdatePriceList;
import com.eventorium.data.solution.models.pricelist.PriceListItem;
import com.eventorium.data.solution.services.PriceListService;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceListRepository {

    private final PriceListService priceListService;

    @Inject
    public PriceListRepository(PriceListService priceListService) {
        this.priceListService = priceListService;
    }

    public LiveData<Result<List<PriceListItem>>> getServices() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getServices().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<List<PriceListItem>>> getProducts() {
        MutableLiveData<Result<List<PriceListItem>>> result = new MutableLiveData<>();
        priceListService.getProducts().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateService(Long id, UpdatePriceList dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateService(id, dto).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<Result<PriceListItem>> updateProduct(Long id, UpdatePriceList dto) {
        MutableLiveData<Result<PriceListItem>> result = new MutableLiveData<>();
        priceListService.updateProduct(id, dto).enqueue(handleResponse(result));
        return result;
    }

    private <T> Callback<T> handleResponse(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error(response.message()));
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        };
    }
}
