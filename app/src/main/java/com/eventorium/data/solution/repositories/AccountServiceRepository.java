package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessAsBoolean;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessfulResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.services.AccountServiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

public class AccountServiceRepository {

    private final AccountServiceService service;

    @Inject
    public AccountServiceRepository(AccountServiceService service) {
        this.service = service;
    }

    public LiveData<Result<PagedResponse<ServiceSummary>>> getManageableServices(int size, int page) {
        MutableLiveData<Result<PagedResponse<ServiceSummary>>> result = new MutableLiveData<>();
        service.getManageableServices(size, page).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<ServiceSummary>>> searchServices(String query, int page, int size) {
        MutableLiveData<Result<PagedResponse<ServiceSummary>>> result = new MutableLiveData<>();
        service.searchManageableServices(query, page, size).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<ServiceSummary>>> filterServices(ServiceFilter filter, int page, int size) {
        MutableLiveData<Result<PagedResponse<ServiceSummary>>> result = new MutableLiveData<>();
        service.filterManageableServices(getFilterParams(filter, page, size)).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Boolean> isFavouriteService(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteService(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Result<Void>> addFavouriteService(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addFavouriteService(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Boolean> removeFavouriteService(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        service.removeFavouriteService(id).enqueue(handleSuccessAsBoolean(result));
        return result;
    }

    public LiveData<Result<List<ServiceSummary>>> getFavouriteServices() {
        MutableLiveData<Result<List<ServiceSummary>>> result = new MutableLiveData<>();
        service.getFavouriteServices().enqueue(handleGeneralResponse(result));
        return result;
    }

    private Map<String, String> getFilterParams(ServiceFilter filter, int page, int size) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "category", filter.getCategory());
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "minPrice", filter.getMinPrice());
        addParamIfNotNull(params, "maxPrice", filter.getMaxPrice());
        addParamIfNotNull(params, "availability", filter.getAvailability());
        addParamIfNotNull(params, "page", page);
        addParamIfNotNull(params, "size", size);

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value) {
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }
}
