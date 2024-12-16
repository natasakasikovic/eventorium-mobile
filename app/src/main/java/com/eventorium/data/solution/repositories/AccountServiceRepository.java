package com.eventorium.data.solution.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.services.AccountServiceService;

import java.util.List;

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



}
