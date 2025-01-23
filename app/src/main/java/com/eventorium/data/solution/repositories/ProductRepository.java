package com.eventorium.data.solution.repositories;

import static java.util.stream.Collectors.toList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.services.ProductService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;
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

    public LiveData<Result<Product>> createProduct(CreateProduct request) {
        MutableLiveData<Result<Product>> result = new MutableLiveData<>();
        productService.createProduct(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });
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

        productService.uploadImages(id, parts).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    result.postValue(Result.success(null));
                } else {
                    try {
                        String err = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(err)));
                    } catch (IOException e) {
                        result.postValue(Result.error("Error while uploading images"));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(Result.error("Error while uploading images"));
            }
        });

        return result;
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

    public LiveData<Result<List<ProductSummary>>> getProducts(){
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();

        productService.getAllProducts().enqueue(new Callback<>() {
           @Override
           public void onResponse(@NonNull Call<List<ProductSummary>> call, @NonNull Response<List<ProductSummary>> response) {
               if (response.isSuccessful() && response.body() != null) {
                   liveData.postValue(Result.success(response.body()));
               }
           }
           @Override
           public void onFailure(@NonNull Call<List<ProductSummary>> call,@NonNull Throwable t) {
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

    public LiveData<Result<List<ProductSummary>>> searchProducts(String keyword) {
        MutableLiveData<Result<List<ProductSummary>>> liveData = new MutableLiveData<>();
        productService.searchProducts(keyword).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductSummary>> call, @NonNull Response<List<ProductSummary>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(Result.success(response.body()));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<ProductSummary>> call, @NonNull Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }
}
