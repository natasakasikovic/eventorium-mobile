package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.event.repositories.BudgetRepository;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BudgetViewModel extends ViewModel {

    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final BudgetRepository budgetRepository;

    @Inject
    public BudgetViewModel(
            ProductRepository productRepository,
            ServiceRepository serviceRepository,
            BudgetRepository budgetRepository
    ) {
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.budgetRepository = budgetRepository;
    }


    public LiveData<List<ServiceSummary>> getSuggestedServices(Long categoryId, Long eventId, Double price) {
        return serviceRepository.getSuggestedServices(categoryId, eventId, price);
    }

    public LiveData<List<ProductSummary>> getSuggestedProducts(Long categoryId, Double price) {
        return productRepository.getSuggestedProducts(categoryId, price);
    }

    public LiveData<Result<List<ProductSummary>>> getPurchasedProducts(Long eventId) {
        return budgetRepository.getPurchased(eventId);
    }

    public LiveData<Result<Product>> purchaseProduct(Long eventId, BudgetItem product) {
        return budgetRepository.purchaseProduct(eventId, product);
    }

    public LiveData<Result<Budget>> getBudget(Long eventId) {
        return budgetRepository.getBudget(eventId);
    }
}
