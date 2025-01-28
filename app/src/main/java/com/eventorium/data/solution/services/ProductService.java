package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    @GET("products/all")
    Call<List<ProductSummary>> getAllProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @GET("products/{id}/images")
    Call<List<ImageResponseDto>> getProductImages(@Path("id") Long id);

    @GET("products/{id}/image")
    Call<ResponseBody> getProductImage(@Path("id") Long id);

    @GET("products/suggestions")
    Call<List<ProductSummary>> getSuggestions(@Query("categoryId") Long id, @Query("price") Double price);

    @GET("products/top-five-products")
    Call<List<ProductSummary>> getTopProducts();

    @GET("products/search/all")
    Call<List<ProductSummary>> searchProducts(@Query("keyword") String keyword);

    @POST("products")
    Call<Product> createProduct(@Body CreateProduct product);

    @Multipart
    @POST("products/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);
}
