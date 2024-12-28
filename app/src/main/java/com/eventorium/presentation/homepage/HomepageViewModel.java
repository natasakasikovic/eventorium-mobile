package com.eventorium.presentation.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomepageViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    @Inject
    public HomepageViewModel(EventRepository eventRepository, ProductRepository productRepository){
        this.eventRepository = eventRepository;
        this.productRepository = productRepository;
    }
    public LiveData<Result<List<EventSummary>>> getTopEvents(){
        return eventRepository.getTopEvents();
    }

    public LiveData<Result<List<ProductSummary>>> getTopProducts(){
        return  productRepository.getTopProducts();
    }
}
