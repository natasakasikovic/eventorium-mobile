package com.eventorium.data.solution.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceResponseDto;
import com.eventorium.data.solution.services.ServiceService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {

    private final ServiceService serviceService;

    @Inject
    public ServiceRepository(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public LiveData<Boolean> createService(CreateServiceRequestDto dto) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        serviceService.createService(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ServiceResponseDto>> call,
                    @NonNull Response<List<ServiceResponseDto>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(true);
                } else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<ServiceResponseDto>> call,
                    @NonNull Throwable t
            ) {
                result.postValue(false);
            }
        });
        return result;
    }
}
