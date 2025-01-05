package com.eventorium.presentation.solution.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final ProductRepository productRepository;
    private final AccountProductRepository accountProductRepository;

    @Inject
    public ProductViewModel(
            AccountProductRepository accountProductRepository,
            AuthRepository authRepository,
            ProductRepository productRepository
    ) {
        this.authRepository = authRepository;
        this.productRepository = productRepository;
        this.accountProductRepository = accountProductRepository;
    }

    public LiveData<Product> getProduct(Long id) {
        return productRepository.getProduct(id);
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return productRepository.getProductImage(id);
    }

    public LiveData<List<Bitmap>> getProductImages(Long id) {
        return productRepository.getProductImages(id);
    }

    public boolean isLoggedIn()  {
        return authRepository.isLoggedIn();
    }
    public LiveData<Boolean> isFavourite(Long id) {
        return accountProductRepository.isFavouriteProduct(id);
    }

    public LiveData<Boolean> removeFavouriteProduct(Long id) {
        return accountProductRepository.removeFavouriteProduct(id);
    }

    public LiveData<String> addFavouriteProduct(Long id) {
        return accountProductRepository.addFavouriteProduct(id);
    }

    public LiveData<Result<List<ProductSummary>>> getProducts(){
        return productRepository.getProducts();
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
}
