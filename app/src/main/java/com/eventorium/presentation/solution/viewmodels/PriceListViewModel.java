package com.eventorium.presentation.solution.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.solution.models.pricelist.UpdatePriceList;
import com.eventorium.data.solution.models.pricelist.PriceListItem;
import com.eventorium.data.solution.repositories.PriceListRepository;
import com.eventorium.data.shared.models.Result;

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

    public LiveData<Result<List<PriceListItem>>> getServices() {
        return priceListRepository.getServices();
    }

    public LiveData<Result<List<PriceListItem>>> getProducts() {
        return priceListRepository.getProducts();
    }
    public LiveData<Result<PriceListItem>> updateService(Long id, UpdatePriceList dto) {
        return priceListRepository.updateService(id, dto);
    }

    public LiveData<Result<PriceListItem>> updateProduct(Long id, UpdatePriceList dto) {
        return priceListRepository.updateProduct(id, dto);
    }

    public LiveData<Result<Uri>> downloadPdf(Context context) {
        return priceListRepository.downloadPdf(context);
    }

}
