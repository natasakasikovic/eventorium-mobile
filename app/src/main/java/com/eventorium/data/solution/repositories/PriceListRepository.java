package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.UpdatePriceListRequestDto;
import com.eventorium.data.solution.models.PriceListItem;
import com.eventorium.data.solution.services.PriceListService;

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

    public LiveData<List<PriceListItem>> getServices() {
        MutableLiveData<List<PriceListItem>> result = new MutableLiveData<>();
        priceListService.getServices().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<List<PriceListItem>> getProducts() {
        MutableLiveData<List<PriceListItem>> result = new MutableLiveData<>();
        priceListService.getProducts().enqueue(handleResponse(result));
        return result;
    }

    public LiveData<PriceListItem> updateService(Long id, UpdatePriceListRequestDto dto) {
        MutableLiveData<PriceListItem> result = new MutableLiveData<>();
        priceListService.updateService(id, dto).enqueue(handleResponse(result));
        return result;
    }

    public LiveData<PriceListItem> updateProduct(Long id, UpdatePriceListRequestDto dto) {
        MutableLiveData<PriceListItem> result = new MutableLiveData<>();
        priceListService.updateProduct(id, dto).enqueue(handleResponse(result));
        return result;
    }

    private <T> Callback<T> handleResponse(MutableLiveData<T> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        };
    }
}
