package com.eventorium.presentation.solution.viewmodels;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ServiceViewModel extends ViewModel {
    private final ServiceRepository serviceRepository;
    private final AccountServiceRepository accountServiceRepository;

    @Inject
    public ServiceViewModel(
            ServiceRepository serviceRepository,
            AccountServiceRepository accountServiceRepository
    ) {
        this.serviceRepository = serviceRepository;
        this.accountServiceRepository = accountServiceRepository;
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

    public LiveData<Long> createService(CreateServiceRequestDto dto) {
        return serviceRepository.createService(dto);
    }

    public LiveData<Boolean> uploadImages(Long serviceId, Context context, List<Uri> uris) {
        return serviceRepository.uploadImages(serviceId, context, uris);
    }

    public LiveData<Boolean> isFavourite(Long id) {
        return accountServiceRepository.isFavouriteService(id);
    }

    public LiveData<Boolean> removeFavouriteService(Long id) {
        return accountServiceRepository.removeFavouriteService(id);
    }

    public LiveData<String> addFavouriteService(Long id) {
        return accountServiceRepository.addFavouriteService(id);
    }
}
