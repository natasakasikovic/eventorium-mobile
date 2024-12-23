package com.eventorium.presentation.solution.viewmodels;


import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.util.Result;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ServiceViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final ServiceRepository serviceRepository;
    @Inject
    public ServiceViewModel(AuthRepository authRepository, ServiceRepository serviceRepository) {
        this.authRepository = authRepository;
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Service> getService(Long id) {
        return serviceRepository.getService(id);
    }

    public LiveData<Bitmap> getServiceImage(Long id) {
        return serviceRepository.getServiceImage(id);
    }

    public LiveData<List<Bitmap>> getServiceImages(Long id) {
        return serviceRepository.getServiceImages(id);
    }

    public LiveData<Result<ServiceSummary>> updateService(Long serviceId, UpdateServiceRequestDto dto) {
        return serviceRepository.updateService(serviceId, dto);
    }

    public LiveData<Long> createService(CreateServiceRequestDto dto) {
        return serviceRepository.createService(dto);
    }

    public LiveData<Boolean> uploadImages(Long serviceId, Context context, List<Uri> uris) {
        return serviceRepository.uploadImages(serviceId, context, uris);
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
}
