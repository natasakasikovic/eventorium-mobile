package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableServiceViewModel extends ViewModel {

    private final AccountServiceRepository repository;

    @Inject
    public ManageableServiceViewModel(AccountServiceRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<ServiceSummary>>> getServices() {
        return repository.getManageableServices();
    }

    public LiveData<Result<List<ServiceSummary>>> searchServices(String keyword) {
        return repository.searchServices(keyword);
    }

    public LiveData<Result<List<ServiceSummary>>> filterServices(ServiceFilter filter) {
        return repository.filterServices(filter);
    }
}
