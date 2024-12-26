package com.eventorium.presentation.solution.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.dtos.UpdatePriceListRequestDto;
import com.eventorium.data.solution.models.PriceListItem;
import com.eventorium.data.solution.repositories.PriceListRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PriceListViewModel extends ViewModel {
    private final PriceListRepository priceListRepository;


    @Inject
    public PriceListViewModel(PriceListRepository priceListRepository) {
        this.priceListRepository = priceListRepository;
    }

    public LiveData<List<PriceListItem>> getServices() {
        return priceListRepository.getServices();
    }

    public LiveData<List<PriceListItem>> getProducts() {
        return priceListRepository.getProducts();
    }
    public LiveData<PriceListItem> updateService(Long id, UpdatePriceListRequestDto dto) {
        return priceListRepository.updateService(id, dto);
    }

    public LiveData<PriceListItem> updateProduct(Long id, UpdatePriceListRequestDto dto) {
        return priceListRepository.updateProduct(id, dto);
    }
}
