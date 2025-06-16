package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.product.ProductSummary;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AccountProductService {

    @GET("account/products/favourites/{id}")
    Call<Boolean> isFavouriteProduct(@Path("id") Long id);

    @GET("account/products/favourites")
    Call<List<ProductSummary>> getFavouriteProducts();

    @POST("account/products/favourites/{id}")
    Call<ResponseBody> addFavouriteProduct(@Path("id") Long id);

    @DELETE("account/products/favourites/{id}")
    Call<ResponseBody> removeFavouriteProduct(@Path("id") Long id);

    @GET("account/products/all")
    Call<List<ProductSummary>> getProducts();

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Long id);

    @GET("account/products/search/all")
    Call<List<ProductSummary>> searchProducts(@Query("keyword") String keyword);

    @GET("account/products/filter/all")
    Call<List<ProductSummary>> filterProducts(@QueryMap Map<String, String> params);
}
