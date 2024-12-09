package com.eventorium.presentation.solution.viewmodels;


import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.repositories.ServiceRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ServiceViewModel extends ViewModel {
    private final ServiceRepository serviceRepository;

    @Inject
    public ServiceViewModel(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Long> createService(CreateServiceRequestDto dto) {
        return serviceRepository.createService(dto);
    }

    public LiveData<Boolean> uploadImages(Long serviceId, Context context, List<Uri> uris) {
        return serviceRepository.uploadImages(serviceId, context, uris);
    }
}
