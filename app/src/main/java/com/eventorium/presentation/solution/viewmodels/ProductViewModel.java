package com.eventorium.presentation.solution.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductViewModel extends ViewModel {

    private final ProductRepository productRepository;
    private final AccountProductRepository accountProductRepository;


    @Inject
    public ProductViewModel(
            AccountProductRepository accountProductRepository,
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
        this.accountProductRepository = accountProductRepository;
    }

    public LiveData<Result<Product>> createProduct(CreateProduct product) {
        return productRepository.createProduct(product);
    }

    public LiveData<Result<Void>> uploadImages(Long id, Context context, List<Uri> uris) {
        return productRepository.uploadImages(id, context, uris);
    }

    public LiveData<Product> getProduct(Long id) {
        return productRepository.getProduct(id);
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return productRepository.getProductImage(id);
    }

    public LiveData<Result<List<ImageItem>>> getProductImages(Long id) {
        return productRepository.getProductImages(id);
    }

    public LiveData<Boolean> isFavourite(Long id) {
        return accountProductRepository.isFavouriteProduct(id);
    }

    public LiveData<Boolean> removeFavouriteProduct(Long id) {
        return accountProductRepository.removeFavouriteProduct(id);
    }

    public LiveData<Result<Void>> addFavouriteProduct(Long id) {
        return accountProductRepository.addFavouriteProduct(id);
    }

    public LiveData<Result<List<ProductSummary>>> getProducts(){
        return productRepository.getProducts();
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        return productRepository.filterProducts(filter);
    }
}
