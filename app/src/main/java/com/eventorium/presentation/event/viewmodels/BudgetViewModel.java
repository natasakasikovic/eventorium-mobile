package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BudgetViewModel extends ViewModel {

    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;

    @Inject
    public BudgetViewModel(ProductRepository productRepository, ServiceRepository serviceRepository) {
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }


    public LiveData<List<ServiceSummary>> getSuggestedServices(Long categoryId, Double price) {
        return serviceRepository.getSuggestedServices(categoryId, price);
    }

    public LiveData<List<ProductSummary>> getSuggestedProducts(Long categoryId, Double price) {
        return productRepository.getSuggestedProducts(categoryId, price);
    }

}
