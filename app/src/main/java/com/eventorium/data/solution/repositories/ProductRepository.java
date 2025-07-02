package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;
import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.product.UpdateProduct;
import com.eventorium.data.solution.services.ProductService;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.MultipartBody;

public class ProductRepository {

    private final ProductService service;

    @Inject
    public ProductRepository(ProductService service) {
        this.service = service;
    }

    public LiveData<Result<Product>> createProduct(CreateProduct request) {
        MutableLiveData<Result<Product>> result = new MutableLiveData<>();
        service.createProduct(request).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Void>> uploadImages(Long id, Context context, List<Uri> uris) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        List<MultipartBody.Part> parts;

        try {
            parts = FileUtil.getImagesFromUris(context, uris, "images");
        } catch (IOException e) {
            result.postValue(Result.error("Error while uploading images."));
            return result;
        }

        service.uploadImages(id, parts).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<Product>> updateProduct(Long id, UpdateProduct request) {
        MutableLiveData<Result<Product>> result = new MutableLiveData<>();
        service.updateProduct(id, request).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Product> getProduct(Long id) {
        MutableLiveData<Product> result = new MutableLiveData<>();
        service.getProduct(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        service.getProductImage(id).enqueue(handleGetImage(result));
        return result;
    }
    public LiveData<Result<List<ImageItem>>> getProductImages(Long id) {
        MutableLiveData<Result<List<ImageItem>>> liveData = new MutableLiveData<>();
        service.getProductImages(id).enqueue(handleGetImages(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteProduct(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.deleteProduct(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<Void>> deleteImages(Long id, List<RemoveImageRequest> request) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        service.deleteImages(id, request).enqueue(handleVoidResponse(liveData));
        return liveData;
    }


    public LiveData<Result<List<ProductSummary>>> getTopProducts(){
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();
        service.getTopProducts().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> getProducts(int page, int size){
        MutableLiveData<Result<PagedResponse<ProductSummary>>> liveData = new MutableLiveData<>();
        service.getProducts(page, size).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> searchProducts(String keyword, int page, int size) {
        MutableLiveData<Result<PagedResponse<ProductSummary>>> liveData = new MutableLiveData<>();
        service.searchProducts(keyword, page, size).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<PagedResponse<ProductSummary>>> filterProducts(ProductFilter filter, int page, int size) {
        MutableLiveData<Result<PagedResponse<ProductSummary>>> result = new MutableLiveData<>();
        service.filterProducts(getFilterParams(filter, page, size)).enqueue(handleGeneralResponse(result));
        return result;
    }
    private Map<String, String> getFilterParams(ProductFilter filter, int page, int size) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "category", filter.getCategory());
        addParamIfNotNull(params, "page", page);
        addParamIfNotNull(params, "size", size);
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "minPrice", filter.getMinPrice());
        addParamIfNotNull(params, "maxPrice", filter.getMaxPrice());
        addParamIfNotNull(params, "availability", filter.getAvailability());;

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value) {
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }

}
