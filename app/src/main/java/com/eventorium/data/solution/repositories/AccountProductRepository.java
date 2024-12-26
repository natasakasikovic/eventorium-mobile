package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.Product;
import com.eventorium.data.solution.services.ProductService;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountProductRepository {

    private final ProductService service;

    @Inject
    public AccountProductRepository(ProductService service) {
        this.service = service;
    }

    public LiveData<Boolean> isFavouriteProduct(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteProduct(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Boolean> call,
                    @NonNull Response<Boolean> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
        return result;
    }

    public LiveData<String> addFavouriteProduct(Long id) {
        MutableLiveData<String> result = new MutableLiveData<>();
        service.addFavouriteProduct(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Product> call,
                    @NonNull Response<Product> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body().getName());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return result;
    }

    public LiveData<Boolean> removeFavouriteProduct(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        service.removeFavouriteProduct(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if(response.isSuccessful()) {
                    result.postValue(true);
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return result;
    }
}
