package com.eventorium.presentation.homepage;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.event.repositories.EventTypeRepository;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomepageViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final EventTypeRepository eventTypeRepository;

    @Inject
    public HomepageViewModel(
            EventRepository eventRepository,
            ProductRepository productRepository,
            ServiceRepository serviceRepository,
            EventTypeRepository eventTypeRepository
    ){
        this.eventTypeRepository = eventTypeRepository;
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }
    public LiveData<Result<List<EventSummary>>> getTopEvents(){
        return eventRepository.getTopEvents();
    }

    public LiveData<Result<List<ProductSummary>>> getTopProducts(){
        return  productRepository.getTopProducts();
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return productRepository.getProductImage(id);
    }

    public LiveData<Result<List<ServiceSummary>>> getTopServices(){
        return serviceRepository.getTopServices();
    }

    public LiveData<Bitmap> getServiceImage(Long id){
        return serviceRepository.getServiceImage(id);
    }

    public LiveData<Bitmap> getEventImage(Long id) {
        return eventTypeRepository.getImage(id);
    }
}
