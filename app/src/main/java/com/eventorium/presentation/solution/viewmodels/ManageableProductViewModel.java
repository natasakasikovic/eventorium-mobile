package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableProductViewModel extends PagedViewModel<ProductSummary, ProductFilter> {
    private final AccountProductRepository repository;
    @Inject
    public ManageableProductViewModel(AccountProductRepository repository) {
        super(2);
        this.repository = repository;
    }

    @Override
    protected LiveData<Result<PagedResponse<ProductSummary>>> loadPage(PagingMode mode, int page, int size) {
        return switch (mode) {
            case DEFAULT -> repository.getProducts(page, size);
            case SEARCH -> repository.searchProducts(searchQuery, page, size);
            case FILTER -> repository.filterProducts(filterParams, page, size);
        };
    }
}
