package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.repositories.BudgetRepository;
import com.eventorium.data.interaction.models.review.SolutionReview;
import com.eventorium.data.event.models.Budget;
import com.eventorium.data.event.models.BudgetItem;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.solution.repositories.ServiceRepository;

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
            BudgetRepository budgetRepository,
            ProductRepository productRepository,
            ServiceRepository serviceRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Result<Product>> purchaseProduct(Long eventId, BudgetItem product) {
        return budgetRepository.purchaseProduct(eventId, product);
    }

    public LiveData<Result<Budget>> getBudget(Long eventId) {
        return budgetRepository.getBudget(eventId);
    }
    public LiveData<Result<List<SolutionReview>>> getBudgetItems() {
        return budgetRepository.getBudgetItems();
    }

}
