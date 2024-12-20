package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.Product;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {

    @GET("products/all")
    Call<List<ProductSummary>> getAllProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @GET("products/{id}/images")
    Call<List<ImageResponseDto>> getProductImages(@Path("id") Long id);

    @GET("products/{id}/image")
    Call<ResponseBody> getProductImage(@Path("id") Long id);
}
