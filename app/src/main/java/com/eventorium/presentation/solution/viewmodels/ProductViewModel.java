package com.eventorium.presentation.solution.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.product.UpdateProduct;
import com.eventorium.data.solution.repositories.AccountProductRepository;
import com.eventorium.data.solution.repositories.ProductRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.models.RemoveImageRequest;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProductViewModel extends PagedViewModel<ProductSummary, ProductFilter> {

    private final ProductRepository repository;
    private final AccountProductRepository accountProductRepository;

    @Inject
    public ProductViewModel(
            AccountProductRepository accountProductRepository,
            ProductRepository productRepository
    ) {
        super(2);
        this.repository = productRepository;
        this.accountProductRepository = accountProductRepository;
    }

    public LiveData<Result<Product>> createProduct(CreateProduct product) {
        return repository.createProduct(product);
    }

    public LiveData<Result<Void>> deleteProduct(Long id) {
        return repository.deleteProduct(id);
    }

    public LiveData<Result<Void>> uploadImages(Long id, Context context, List<Uri> uris) {
        return repository.uploadImages(id, context, uris);
    }

    public LiveData<Result<Product>> updateProduct(Long id, UpdateProduct request) {
        return repository.updateProduct(id, request);
    }

    public LiveData<Product> getProduct(Long id) {
        return repository.getProduct(id);
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        return repository.getProductImage(id);
    }

    public LiveData<Result<List<ImageItem>>> getProductImages(Long id) {
        return repository.getProductImages(id);
    }

    public LiveData<Result<Void>> removeImages(Long id, List<RemoveImageRequest> removedImages) {
        return repository.deleteImages(id, removedImages);
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

    @Override
    protected LiveData<Result<PagedResponse<ProductSummary>>> loadPage(PagingMode mode, int page, int size) {
        return switch (mode) {
            case DEFAULT -> repository.getProducts(page, size);
            case SEARCH -> repository.searchProducts(searchQuery, page, size);
            case FILTER -> repository.filterProducts(filterParams, page, size);
        };
    }
}
