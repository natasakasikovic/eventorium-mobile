package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableProductViewModel extends ViewModel {
    private final AccountProductRepository repository;

    @Inject
    public ManageableProductViewModel(AccountProductRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<ProductSummary>>> getProducts() {
        return repository.getProducts();
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        return repository.searchProducts(keyword);
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        return repository.filterProducts(filter);
    }

    public LiveData<Result<Void>> deleteProduct(Long id) {
        return repository.deleteProduct(id);
    }
}
