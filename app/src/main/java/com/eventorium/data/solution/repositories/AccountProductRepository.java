package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.services.AccountProductService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;

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
        service.isFavouriteProduct(id).enqueue(handleBooleanResponse(result));
        return result;
    }

    public LiveData<Result<Void>> addFavouriteProduct(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addFavouriteProduct(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Boolean> removeFavouriteProduct(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        service.removeFavouriteProduct(id).enqueue(handleBooleanResponse(result));
        return result;
    }

    public LiveData<Result<List<ProductSummary>>> getFavouriteProducts() {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.getFavouriteProducts().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<ProductSummary>>> getProducts() {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.getProducts().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.searchProducts(keyword).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.filterProducts(getFilterParams(filter)).enqueue(handleGeneralResponse(result));
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
