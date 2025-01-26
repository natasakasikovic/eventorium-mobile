package com.eventorium.presentation.favourites.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavouritesViewModel extends ViewModel {

    private final AccountEventRepository eventRepository;
    private final AccountProductRepository accountProductRepository;
    private final ProductRepository productRepository;
    private final AccountServiceRepository accountServiceRepository;
    private final ServiceRepository serviceRepository;

    @Inject
    public FavouritesViewModel(AccountEventRepository eventRepository,
                               AccountProductRepository accountProductRepository,
                               ProductRepository productRepository,
                               AccountServiceRepository accountServiceRepository,
                               ServiceRepository serviceRepository)
    {
        this.eventRepository = eventRepository;
        this.accountProductRepository = accountProductRepository;
        this.productRepository = productRepository;
        this.accountServiceRepository = accountServiceRepository;
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Result<List<EventSummary>>> getFavouriteEvents() {
        return eventRepository.getFavouriteEvents();
    }

    public LiveData<Result<List<ProductSummary>>> getFavouriteProducts() {
        return accountProductRepository.getFavouriteProducts();
    }

    public LiveData<Result<List<ServiceSummary>>> getFavouriteServices() {
        return accountServiceRepository.getFavouriteServices();
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return productRepository.getProductImage(id);
    }

    public LiveData<Bitmap> getServiceImage(Long id) {
        return serviceRepository.getServiceImage(id);
    }
}
