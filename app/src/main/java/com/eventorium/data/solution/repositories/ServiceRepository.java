package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;
import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.service.CalendarReservation;
import com.eventorium.data.solution.models.service.CreateService;
import com.eventorium.data.solution.models.service.UpdateService;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.models.ImageResponse;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

import java.io.IOException;
import java.util.Collections;
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

    public LiveData<Result<ServiceSummary>> createService(CreateService dto) {
        MutableLiveData<Result<ServiceSummary>> result = new MutableLiveData<>();
        serviceService.createService(dto).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Service> getService(Long id) {
        MutableLiveData<Service> result = new MutableLiveData<>();
        serviceService.getService(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Bitmap> getServiceImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        serviceService.getServiceImage(id).enqueue(handleGetImage(result));
        return result;
    }

    public LiveData<Boolean> uploadImages(Long serviceId, Context context, List<Uri> uris) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        List<MultipartBody.Part> parts;

        try {
            parts = FileUtil.getImagesFromUris(context, uris, "images");
        } catch (IOException e) {
            Log.e("IMAGES_ERROR", Objects.requireNonNull(e.getLocalizedMessage()));
            result.setValue(false);
            return result;
        }
        serviceService.uploadImages(serviceId, parts).enqueue(handleBooleanResponse(result));
        return result;
    }

    public LiveData<Result<Void>> deleteImages(Long id, List<RemoveImageRequest> request) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        serviceService.deleteImages(id, request).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ImageItem>>> getServiceImages(Long id) {
        MutableLiveData<Result<List<ImageItem>>> liveData = new MutableLiveData<>();
        serviceService.getServiceImages(id).enqueue(handleGetImages(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteService(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        serviceService.deleteService(id).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Service>> updateService(Long serviceId, UpdateService dto) {
        MutableLiveData<Result<Service>> liveData = new MutableLiveData<>();
        serviceService.updateService(serviceId, dto).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ServiceSummary>>> getTopServices(){
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        serviceService.getTopServices().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }


    public LiveData<Result<List<ServiceSummary>>> getServices() {
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        serviceService.getServices().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<List<ServiceSummary>> getSuggestedServices(Long categoryId, Double price) {
        MutableLiveData<List<ServiceSummary>> liveData = new MutableLiveData<>(Collections.emptyList());
        serviceService.getSuggestions(categoryId, price).enqueue(handleSuccessfulResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ServiceSummary>>> searchServices(String keyword) {
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        serviceService.searchServices(keyword).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<CalendarReservation>>> getReservations() {
        MutableLiveData<Result<List<CalendarReservation>>> result = new MutableLiveData<>();
        serviceService.getReservations().enqueue(handleGeneralResponse(result));
        return result;
    }

}
