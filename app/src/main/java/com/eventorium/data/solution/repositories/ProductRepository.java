package com.eventorium.data.solution.repositories;

import static java.util.stream.Collectors.toList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.Product;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.solution.services.ProductService;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {

    private final ProductService productService;


    @Inject
    public ProductRepository(ProductService productService) {
        this.productService = productService;
    }


    public LiveData<Product> getProduct(Long id) {
        MutableLiveData<Product> result = new MutableLiveData<>();
        productService.getProduct(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Product> call,
                    @NonNull Response<Product> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Product> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                result.postValue(null);
            }
        });

        return result;
    }

    public LiveData<Bitmap> getProductImage(Long id) {
        MutableLiveData<Bitmap> result = new MutableLiveData<>();
        productService.getProductImage(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()){
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        result.postValue(bitmap);
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error decoding image: " + e.getMessage());
                        result.postValue(null);
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    result.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                result.postValue(null);
            }
        });

        return result;
    }
    public LiveData<List<Bitmap>> getProductImages(Long id) {
        MutableLiveData<List<Bitmap>> liveData = new MutableLiveData<>();

        productService.getProductImages(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ImageResponseDto>> call,
                    @NonNull Response<List<ImageResponseDto>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body().stream()
                            .map(ImageResponseDto::getData)
                            .map(FileUtil::convertToBitmap)
                            .collect(toList())
                    );
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<ImageResponseDto>> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                liveData.postValue(null);
            }
        });

        return liveData;
    }

    public LiveData<Result<List<ProductSummary>>> getTopProducts(){
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();

        productService.getTopProducts().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ProductSummary>> call, Response<List<ProductSummary>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                }
            }
            @Override
            public void onFailure(Call<List<ProductSummary>> call, Throwable t) {
                liveData.postValue(Result.error("Oops! Something went wrong! Please, try again later!"));
            }
        });
        return liveData;
    }

    public LiveData<List<ProductSummary>> getSuggestedProducts(Long categoryId, Double price) {
        MutableLiveData<List<ProductSummary>> liveData = new MutableLiveData<>(Collections.emptyList());
        productService.getSuggestions(categoryId, price).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ProductSummary>> call,
                    @NonNull Response<List<ProductSummary>> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductSummary>> call, @NonNull Throwable t) {

            }
        });
        return liveData;
    }
}
