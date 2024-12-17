package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.ServiceFilterDto;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.services.AccountServiceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountServiceRepository {

    private final AccountServiceService service;

    @Inject
    public AccountServiceRepository(AccountServiceService service) {
        this.service = service;
    }

    public LiveData<List<ServiceSummary>> getManageableServices() {
        MutableLiveData<List<ServiceSummary>> liveData = new MutableLiveData<>();
        service.getManageableServices().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ServiceSummary>> call,
                    @NonNull Response<List<ServiceSummary>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServiceSummary>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<ServiceSummary>> searchServices(String query) {
        MutableLiveData<List<ServiceSummary>> liveData = new MutableLiveData<>();

        service.searchManageableServices(query).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ServiceSummary>> call,
                    @NonNull Response<List<ServiceSummary>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<ServiceSummary>> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<ServiceSummary>> filterServices(ServiceFilterDto filter) {
        MutableLiveData<List<ServiceSummary>> liveData = new MutableLiveData<>(new ArrayList<>());

        service.filterManageableServices(getFilterParams(filter)).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ServiceSummary>> call,
                    @NonNull Response<List<ServiceSummary>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServiceSummary>> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });


        return liveData;
    }

    private Map<String, String> getFilterParams(ServiceFilterDto filter) {
        Map<String, String> params = new HashMap<>();

        if (filter.getCategory() != null) {
            params.put("category", filter.getCategory());
        }
        if (filter.getEventType() != null) {
            params.put("eventType", filter.getEventType());
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

        return params;
    }
}
