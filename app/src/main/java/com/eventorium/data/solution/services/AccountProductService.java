package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountProductService {

    @GET("account/products/favourites/{id}")
    Call<Boolean> isFavouriteProduct(@Path("id") Long id);

    @GET("account/products/favourites")
    Call<List<ProductSummary>> getFavouriteProducts();

    @POST("account/products/favourites/{id}")
    Call<Product> addFavouriteProduct(@Path("id") Long id);

    @DELETE("account/products/favourites/{id}")
    Call<ResponseBody> removeFavouriteProduct(@Path("id") Long id);

    @GET("account/products/all")
    Call<List<ProductSummary>> getProducts();
}
