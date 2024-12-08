package com.eventorium.presentation.solution.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.repositories.ServiceRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ServiceViewModel extends ViewModel {
    private final ServiceRepository serviceRepository;

    @Inject
    public ServiceViewModel(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Boolean> createService(CreateServiceRequestDto dto) {
        return serviceRepository.createService(dto);
    }

}
