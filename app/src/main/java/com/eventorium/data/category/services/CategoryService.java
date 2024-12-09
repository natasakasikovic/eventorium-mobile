package com.eventorium.data.category.services;

import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryResponseDto;

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
    Call<List<CategoryResponseDto>> getCategories();

    @PUT("categories/{id}")
    Call<CategoryResponseDto> updateCategory(@Path("id") Long id, @Body CategoryRequestDto dto);

    @POST("categories")
    Call<CategoryResponseDto> createCategory(@Body CategoryRequestDto dto);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") Long id);
}
