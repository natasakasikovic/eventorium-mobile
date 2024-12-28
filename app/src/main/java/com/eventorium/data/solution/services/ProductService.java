package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.Product;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.data.util.dtos.ImageResponseDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("account/products/favourites")
    Call<List<ProductSummary>> getFavouriteProduct();

    @GET("account/products/favourites/{id}")
    Call<Boolean> isFavouriteProduct(@Path("id") Long id);

    @GET("products/top-five-products")
    Call<List<ProductSummary>> getTopProducts();

    @POST("account/products/favourites/{id}")
    Call<Product> addFavouriteProduct(@Path("id") Long id);

    @DELETE("account/products/favourites/{id}")
    Call<ResponseBody> removeFavouriteProduct(@Path("id") Long id);
}
