package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.services.AccountServiceService;
import com.eventorium.data.shared.models.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, String> getFilterParams(ServiceFilter filter, Integer page, Integer size) {
        Map<String, String> params = new HashMap<>();

        if (filter.getCategory() != null) {
            params.put("category", filter.getCategory());
        }
        if (filter.getType() != null) {
            params.put("eventType", filter.getType());
        }
        if (filter.getMinPrice() != null) {
            params.put("minPrice", String.valueOf(filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            params.put("maxPrice", String.valueOf(filter.getMaxPrice()));
        }
        if (filter.getAvailability() != null) {
            params.put("availability", String.valueOf(filter.getAvailability()));
        }
        params.put("page", page.toString());
        params.put("size", size.toString());

        return params;
    }
}
