package com.eventorium.data.services;

import com.eventorium.data.models.CategoryRequestDto;
import com.eventorium.data.models.CategoryResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("categories/all")
    Call<List<CategoryResponseDto>> getCategories();

    @PUT("categories/{id}")
    Call<CategoryResponseDto> updateCategory(@Path("id") Long id, @Body CategoryRequestDto dto);
}
