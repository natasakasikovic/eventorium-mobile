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

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableProductViewModel extends ViewModel {
    private final AccountProductRepository repository;
    private final MutableLiveData<Integer> page = new MutableLiveData<>(0);
    private final int pageSize = 2;

    private final MediatorLiveData<List<ProductSummary>> products = new MediatorLiveData<>();

    @Inject
    public ManageableProductViewModel(AccountProductRepository repository) {
        this.repository = repository;
        loadNextPage();
    }

    public LiveData<List<ProductSummary>> getProducts() {
        return products;
    }

    public void loadNextPage() {
        int nextPage = page.getValue() != null ? page.getValue() : 0;
        LiveData<Result<PagedResponse<ProductSummary>>> newPage = repository.getProducts(nextPage, pageSize);

        products.addSource(newPage, result -> {
            if(result.getError() == null) {
                products.setValue(result.getData().getContent());
                products.removeSource(newPage);
                if(!result.getData().getContent().isEmpty())
                    page.setValue(nextPage + 1);
            }
        });
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        return repository.searchProducts(keyword);
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        return repository.filterProducts(filter);
    }
}
