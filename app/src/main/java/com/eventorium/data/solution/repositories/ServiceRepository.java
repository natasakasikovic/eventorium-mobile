package com.eventorium.data.solution.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceResponseDto;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {

    private final ServiceService serviceService;

    @Inject
    public ServiceRepository(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public LiveData<Long> createService(CreateServiceRequestDto dto) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        serviceService.createService(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ServiceResponseDto> call,
                    @NonNull Response<ServiceResponseDto> response
            ) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    result.postValue(response.body().getId());
                } else {
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ServiceResponseDto> call,
                    @NonNull Throwable t
            ) {
                result.postValue(null);
            }
        });
        return result;
    }

    public LiveData<Boolean> uploadImages(Long serviceId, Context context, List<Uri> uris) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        List<MultipartBody.Part> parts;

        try {
            parts = FileUtil.getImagesFromUris(context, uris);
        } catch (IOException e) {
            Log.e("IMAGES_ERROR", Objects.requireNonNull(e.getLocalizedMessage()));
            result.setValue(false);
            return result;
        }

        serviceService.uploadImages(serviceId, parts).enqueue(new Callback<>() {
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
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                result.postValue(false);
            }
        });
        return result;
    }

}
