package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.services.AccountProductService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountProductRepository {

    private final AccountProductService service;

    @Inject
    public AccountProductRepository(AccountProductService service) {
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

    public LiveData<Result<List<ProductSummary>>> getFavouriteProducts() {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.getFavouriteProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductSummary>> call, Response<List<ProductSummary>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else
                    result.postValue(Result.error("Error while loading favourite products"));

            }

            @Override
            public void onFailure(Call<List<ProductSummary>> call, Throwable t) {
                result.postValue(Result.error("Error while loading favourite products"));
            }
        });

        return result;
    }

    public LiveData<Result<List<ProductSummary>>> getProducts() {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();

        service.getProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductSummary>> call, Response<List<ProductSummary>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else {
                    try {
                        String err = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(err)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductSummary>> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();

        service.searchProducts(keyword).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductSummary>> call, Response<List<ProductSummary>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else
                    result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }

            @Override
            public void onFailure(Call<List<ProductSummary>> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();

        service.filterProducts(getFilterParams(filter)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductSummary>> call, @NonNull Response<List<ProductSummary>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else {
                    try {
                        String errResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductSummary>> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });


        return result;
    }

    private Map<String, String> getFilterParams(ProductFilter filter) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "category", filter.getCategory());
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "minPrice", filter.getMinPrice());
        addParamIfNotNull(params, "maxPrice", filter.getMaxPrice());
        addParamIfNotNull(params, "availability", filter.getAvailability());;

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value) {
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }
}
