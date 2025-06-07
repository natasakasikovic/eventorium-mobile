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

import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductFilter;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.services.ProductService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.models.ImageResponse;
import com.eventorium.presentation.shared.models.ImageItem;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public LiveData<Result<List<ProductSummary>>> getTopProducts(){
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();
        service.getTopProducts().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ProductSummary>>> getProducts(){
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();
        service.getAllProducts().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<List<ProductSummary>> getSuggestedProducts(Long categoryId, Double price) {
        MutableLiveData<List<ProductSummary>> liveData = new MutableLiveData<>(Collections.emptyList());
        service.getSuggestions(categoryId, price).enqueue(handleSuccessfulResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();
        service.searchProducts(keyword).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<ProductSummary>>> filterProducts(ProductFilter filter) {
        MutableLiveData<Result<List<ProductSummary>>> result = new MutableLiveData<>();
        service.filterProducts(getFilterParams(filter)).enqueue(handleGeneralResponse(result));
        return result;
    }
    private Map<String, String> getFilterParams(ProductFilter filter) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "category", filter.getCategory());
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
