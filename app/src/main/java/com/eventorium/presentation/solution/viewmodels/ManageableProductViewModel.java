package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableProductViewModel extends ViewModel {
    private final AccountProductRepository accountProductRepository;

    @Inject
    public ManageableProductViewModel(AccountProductRepository accountProductRepository) {
        this.accountProductRepository = accountProductRepository;
    }

    public LiveData<Result<List<ProductSummary>>> getProducts() {
        return accountProductRepository.getProducts();
    }
}
