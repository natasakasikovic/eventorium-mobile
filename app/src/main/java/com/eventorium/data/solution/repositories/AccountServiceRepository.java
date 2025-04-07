package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.services.AccountServiceService;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
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
            public void onFailure(@NonNull Call<List<ServiceSummary>> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<List<ServiceSummary>> filterServices(ServiceFilter filter) {
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

    public LiveData<Boolean> isFavouriteService(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteService(id).enqueue(new Callback<>() {
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

    public LiveData<Result<Void>> addFavouriteService(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addFavouriteService(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (!response.isSuccessful()) {
                    result.postValue(Result.error(response.message()));
                } else {
                    result.postValue(Result.success(null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

    public LiveData<Boolean> removeFavouriteService(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>(false);
        service.removeFavouriteService(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if(response.isSuccessful()) {
                    result.postValue(true);
                } else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                result.postValue(false);
            }
        });

        return result;
    }

    public LiveData<Result<List<ServiceSummary>>> getFavouriteServices() {
        MutableLiveData<Result<List<ServiceSummary>>> result = new MutableLiveData<>();
        service.getFavouriteServices().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ServiceSummary>> call, Response<List<ServiceSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading favourite services"));
                }
            }

            @Override
            public void onFailure(Call<List<ServiceSummary>> call, Throwable t) {
                result.postValue(Result.error("Error while loading favourite services"));
            }
        });
        return result;
    }

    private Map<String, String> getFilterParams(ServiceFilter filter) {
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
