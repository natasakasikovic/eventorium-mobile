package com.eventorium.data.solution.repositories;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.ServiceSummaryResponseDto;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.mappers.ServiceMapper;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.MultipartBody;
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
                    @NonNull Call<ServiceSummaryResponseDto> call,
                    @NonNull Response<ServiceSummaryResponseDto> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body().getId());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ServiceSummaryResponseDto> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                result.postValue(null);
            }
        });
        return result;
    }

    public LiveData<Service> getService(Long id) {
        MutableLiveData<Service> result = new MutableLiveData<>();
        serviceService.getService(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Service> call,
                    @NonNull Response<Service> response)
            {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Service> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                result.postValue(null);
            }
        });

        return result;
    }

    public LiveData<Bitmap> getServiceImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        serviceService.getServiceImage(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()){
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        result.postValue(bitmap);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error decoding image: " + e.getMessage());
                        result.postValue(null);
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
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
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<List<Bitmap>> getServiceImages(Long id) {
        MutableLiveData<List<Bitmap>> liveData = new MutableLiveData<>();

        serviceService.getServiceImages(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ImageResponseDto>> call,
                    @NonNull Response<List<ImageResponseDto>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body().stream()
                            .map(ImageResponseDto::getData)
                            .map(FileUtil::convertToBitmap)
                            .collect(toList())
                    );
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<ImageResponseDto>> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<Result<Void>> deleteService(Long id) {
        MutableLiveData<Result<Void>> successful = new MutableLiveData<>();
        serviceService.deleteService(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Void> call,
                    @NonNull Response<Void> response
            ) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    successful.postValue(Result.error(response.message()));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Void> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                successful.postValue(Result.error(t.getMessage()));
            }
        });
        return successful;
    }

    public LiveData<Result<ServiceSummary>> updateService(Long serviceId, UpdateServiceRequestDto dto) {
        MutableLiveData<Result<ServiceSummary>> liveData = new MutableLiveData<>();
        serviceService.updateService(serviceId, dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Service> call,
                    @NonNull Response<Service> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(ServiceMapper.fromService(response.body())));
                } else {
                    liveData.postValue(Result.error(response.message()));
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Service> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Result.error(t.getMessage()));
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<Result<List<ServiceSummary>>> getTopServices(){
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();

        serviceService.getTopServices().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ServiceSummary>> call, @NonNull Response<List<ServiceSummary>> response) {
                if (response.isSuccessful() && response.body() != null){
                    liveData.postValue(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ServiceSummary>> call, @NonNull Throwable t) {
                liveData.postValue(Result.error("Oops! Error while loading top five services! Please try again later"));
            }
        });
        return liveData;
    }
}
