package com.eventorium.presentation.solution.viewmodels;



import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.service.CreateService;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.models.service.UpdateService;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

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

    public LiveData<Result<List<ImageItem>>> getServiceImages(Long id) {
        return serviceRepository.getServiceImages(id);
    }

    public LiveData<Result<List<ServiceSummary>>> getServices(){
        return serviceRepository.getServices();
    }

    public LiveData<Result<Service>> updateService(Long serviceId, UpdateService dto) {
        return serviceRepository.updateService(serviceId, dto);
    }

    public LiveData<Result<ServiceSummary>> createService(CreateService dto) {
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

    public LiveData<Result<Void>> addFavouriteService(Long id) {
        return accountServiceRepository.addFavouriteService(id);
    }

    public LiveData<Result<List<ServiceSummary>>> searchServices(String keyword) {
        return serviceRepository.searchServices(keyword);
    }

    public LiveData<Result<List<ServiceSummary>>> filterServices(ServiceFilter filter) {
        return serviceRepository.filterServices(filter);
    }

    public LiveData<Result<Void>> deleteService(Long id) {
        return serviceRepository.deleteService(id);
    }

    public LiveData<Result<Void>> removeImages(Long id, List<RemoveImageRequest> removedImages) {
        return serviceRepository.deleteImages(id, removedImages);
    }
}
