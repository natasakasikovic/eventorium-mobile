package com.eventorium.data.category.services;

import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("categories/all")
    Call<List<Category>> getCategories();

    @PUT("categories/{id}")
    Call<Category> updateCategory(@Path("id") Long id, @Body CategoryRequest dto);

    @POST("categories")
    Call<Category> createCategory(@Body CategoryRequest dto);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") Long id);
}
