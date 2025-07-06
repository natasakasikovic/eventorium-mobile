package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;


import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableProductViewModel extends PagedViewModel<ProductSummary, ProductFilter> {
    private final AccountProductRepository repository;
    @Inject
    public ManageableProductViewModel(AccountProductRepository repository) {
        this.repository = repository;
    }

    @Override
    protected LiveData<Result<PagedResponse<ProductSummary>>> loadPage(PagingMode mode, int page, int size) {
        return switch (mode) {
            case DEFAULT -> repository.getProducts(page, size);
            case SEARCH -> repository.searchProducts(searchQuery, page, size);
            case FILTER -> repository.filterProducts(filterParams, page, size);
            case SORT -> throw new UnsupportedOperationException("Manageable products sort not supported");
        };
    }
}
