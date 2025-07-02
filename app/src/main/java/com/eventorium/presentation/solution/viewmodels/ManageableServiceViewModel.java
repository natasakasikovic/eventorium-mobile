package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableServiceViewModel extends PagedViewModel<ServiceSummary, ServiceFilter> {

    private final AccountServiceRepository repository;

    @Inject
    public ManageableServiceViewModel(AccountServiceRepository repository) {
        super(2);
        this.repository = repository;
    }

    @Override
    protected LiveData<Result<PagedResponse<ServiceSummary>>> loadPage(PagingMode mode, int page, int size) {
        return switch (mode) {
            case DEFAULT -> repository.getManageableServices(page, size);
            case SEARCH -> repository.searchServices(searchQuery, page, size);
            case FILTER -> repository.filterServices(filterParams, page, size);
        };
    }
}
