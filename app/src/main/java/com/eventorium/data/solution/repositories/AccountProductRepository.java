package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessAsBoolean;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessfulResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.services.AccountProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

public class AccountProductRepository {

    private final AccountProductService service;

    @Inject
    public AccountProductRepository(AccountProductService service) {
        this.service = service;
    }

    public LiveData<Boolean> isFavouriteProduct(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteProduct(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Result<Void>> addFavouriteProduct(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addFavouriteProduct(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Boolean> removeFavouriteProduct(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        service.removeFavouriteProduct(id).enqueue(handleSuccessAsBoolean(result));
        return result;
    }

    public LiveData<Result<List<ProductSummary>>> getFavouriteProducts() {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.getFavouriteProducts().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> getProducts(int page, int size) {
        MutableLiveData<Result<PagedResponse<ProductSummary>>> result = new MutableLiveData<>();
        service.getProducts(page, size).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> searchProducts(String keyword, int page, int size) {
        MutableLiveData<Result<PagedResponse<ProductSummary>>> result = new MutableLiveData<>();
        service.searchProducts(keyword, page, size).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> filterProducts(ProductFilter filter, int page, int size) {
        MutableLiveData<Result<PagedResponse<ProductSummary>>> result = new MutableLiveData<>();
        service.filterProducts(getFilterParams(filter, page, size)).enqueue(handleGeneralResponse(result));
        return result;
    }

    private Map<String, String> getFilterParams(ProductFilter filter, int page, int size) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "page", page);
        addParamIfNotNull(params, "size", size);
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
