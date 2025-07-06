package com.eventorium.data.solution.services;

import com.eventorium.data.shared.models.ImageResponse;
import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.solution.models.product.CreateProduct;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.data.solution.models.product.UpdateProduct;
import com.eventorium.presentation.shared.models.RemoveImageRequest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ProductService {

    @GET("products")
    Call<PagedResponse<ProductSummary>> getProducts(@Query("page") int page, @Query("size") int size);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") Long id, @Body UpdateProduct request);

    @GET("products/{id}/images")
    Call<List<ImageResponse>> getProductImages(@Path("id") Long id);

    @GET("products/{id}/image")
    Call<ResponseBody> getProductImage(@Path("id") Long id);

    @GET("products/top-five-products")
    Call<List<ProductSummary>> getTopProducts();

    @GET("products/search")
    Call<PagedResponse<ProductSummary>> searchProducts(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );

    @POST("products")
    Call<Product> createProduct(@Body CreateProduct product);

    @Multipart
    @POST("products/{id}/images")
    Call<ResponseBody> uploadImages(@Path("id") Long id, @Part List<MultipartBody.Part> images);

    @HTTP(method = "DELETE", path = "products/{id}/images", hasBody = true)
    Call<ResponseBody> deleteImages(@Path("id") Long id, @Body List<RemoveImageRequest> images);

    @GET("products/filter")
    Call<PagedResponse<ProductSummary>> filterProducts(@QueryMap Map<String, String> params);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Long id);
}
