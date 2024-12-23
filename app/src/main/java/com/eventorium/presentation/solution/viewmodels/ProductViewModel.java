package com.eventorium.presentation.solution.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.solution.models.Product;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.models.Service;
import com.eventorium.data.solution.repositories.ProductRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final ProductRepository productRepository;

    @Inject
    public ProductViewModel(AuthRepository authRepository, ProductRepository productRepository) {
        this.authRepository = authRepository;
        this.productRepository = productRepository;
    }

    public LiveData<Product> getProduct(Long id) {
        return productRepository.getProduct(id);
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return productRepository.getProductImage(id);
    }

    public LiveData<List<Bitmap>> getServiceImages(Long id) {
        return productRepository.getProductImages(id);
    }

    public boolean isLoggedIn()  {
        return authRepository.isLoggedIn();
    }
}
