package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.service.CalendarReservation;
import com.eventorium.data.solution.models.service.CreateService;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.UpdateService;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class ServiceRepository {

    private final ServiceService service;

    @Inject
    public ServiceRepository(ServiceService service) {
        this.service = service;
    }

    public LiveData<Result<ServiceSummary>> createService(CreateService dto) {
        MutableLiveData<Result<ServiceSummary>> result = new MutableLiveData<>();
        service.createService(dto).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Service> getService(Long id) {
        MutableLiveData<Service> result = new MutableLiveData<>();
        service.getService(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Bitmap> getServiceImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        service.getServiceImage(id).enqueue(handleGetImage(result));
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
        service.uploadImages(serviceId, parts).enqueue(handleSuccessAsBoolean(result));
        return result;
    }

    public LiveData<Result<Void>> deleteImages(Long id, List<RemoveImageRequest> request) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        service.deleteImages(id, request).enqueue(handleVoidResponse(liveData));
        return liveData;
    }


    public LiveData<Result<List<ImageItem>>> getServiceImages(Long id) {
        MutableLiveData<Result<List<ImageItem>>> liveData = new MutableLiveData<>();
        service.getServiceImages(id).enqueue(handleGetImages(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteService(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        service.deleteService(id).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Service>> updateService(Long serviceId, UpdateService dto) {
        MutableLiveData<Result<Service>> liveData = new MutableLiveData<>();
        service.updateService(serviceId, dto).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ServiceSummary>>> getTopServices(){
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        service.getTopServices().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }


    public LiveData<Result<List<ServiceSummary>>> getServices() {
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        service.getServices().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ServiceSummary>>> searchServices(String keyword) {
        MutableLiveData<Result<List<ServiceSummary>>> liveData = new MutableLiveData<>();
        service.searchServices(keyword).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<CalendarReservation>>> getReservations() {
        MutableLiveData<Result<List<CalendarReservation>>> result = new MutableLiveData<>();
        service.getReservations().enqueue(handleGeneralResponse(result));
        return result;
    }


    public LiveData<Result<List<ServiceSummary>>> filterServices(ServiceFilter filter) {
        MutableLiveData<Result<List<ServiceSummary>>> result = new MutableLiveData<>();
        service.filterServices(getFilterParams(filter)).enqueue(handleGeneralResponse(result));
        return result;
    }

    private Map<String, String> getFilterParams(ServiceFilter filter) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "category", filter.getCategory());
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "minPrice", filter.getMinPrice());
        addParamIfNotNull(params, "maxPrice", filter.getMaxPrice());
        addParamIfNotNull(params, "availability", filter.getAvailability());;

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value){
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }

}