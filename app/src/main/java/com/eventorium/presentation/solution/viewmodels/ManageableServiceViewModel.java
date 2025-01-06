package com.eventorium.presentation.solution.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.service.ServiceFilter;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.data.solution.repositories.AccountServiceRepository;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.util.Result;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableServiceViewModel extends ViewModel {

    private final AccountServiceRepository repository;
    private final ServiceRepository serviceRepository;
    private final MutableLiveData<List<ServiceSummary>> manageableServices = new MutableLiveData<>();
    private final MutableLiveData<List<ServiceSummary>> filterResults = new MutableLiveData<>();
    private final MutableLiveData<List<ServiceSummary>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    @Inject
    public ManageableServiceViewModel(AccountServiceRepository repository, ServiceRepository serviceRepository) {
        this.repository = repository;
        this.serviceRepository = serviceRepository;
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

    public void filterServices(ServiceFilter filter) {
        isLoading.setValue(true);
        repository.filterServices(filter).observeForever(services -> {
            filterResults.postValue(services);
            isLoading.setValue(false);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.getManageableServices().removeObserver(manageableServices::postValue);
        repository.searchServices(null).removeObserver(searchResults::postValue);
        repository.filterServices(new ServiceFilter()).removeObserver(filterResults::postValue);
    }

    public LiveData<List<ServiceSummary>> getFilterResults() {
        return filterResults;
    }


    public LiveData<Result<Void>> deleteService(Long serviceId) {
        LiveData<Result<Void>> result =  serviceRepository.deleteService(serviceId);
        if(result != null && Objects.requireNonNull(result.getValue()).getError() != null) {
            manageableServices.postValue(Objects.requireNonNull(manageableServices.getValue())
                    .stream()
                    .filter(category -> !Objects.equals(category.getId(), serviceId))
                    .collect(toList()));
        }
        return result;
    }

    public void setFilterResults(List<ServiceSummary> filterResults) {
        this.filterResults.setValue(filterResults);
    }

}
