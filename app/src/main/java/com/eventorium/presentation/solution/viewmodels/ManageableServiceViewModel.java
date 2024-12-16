package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.services.ServiceService;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableServiceViewModel extends ViewModel {

    private final AccountServiceRepository repository;
    private MutableLiveData<List<ServiceSummary>> manageableServices = new MutableLiveData<>();
    private MutableLiveData<List<ServiceSummary>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    @Inject
    public ManageableServiceViewModel(AccountServiceRepository repository) {
        this.repository = repository;
        fetchManageableServices();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private void fetchManageableServices() {
        isLoading.postValue(false);
        repository.getManageableServices().observeForever(services -> {
            this.manageableServices.postValue(services);
            isLoading.postValue(false);
        });
    }

    public LiveData<List<ServiceSummary>> getSearchResults() {
        return searchResults;
    }
    public LiveData<List<ServiceSummary>> getManageableServices() {
        return manageableServices;
    }

    public void searchServices(String query) {
        if (query == null || query.trim().isEmpty()) {
            searchResults.postValue(manageableServices.getValue());
            return;
        }

        isLoading.setValue(true);
        repository.searchServices(query).observeForever(services -> {
            searchResults.postValue(services);
            isLoading.postValue(false);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.getManageableServices().removeObserver(services -> manageableServices.postValue(services));
        repository.searchServices(null).removeObserver(services -> searchResults.postValue(services));
    }
}
