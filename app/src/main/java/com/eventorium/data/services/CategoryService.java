package com.eventorium.data.services;

import com.eventorium.data.models.CategoryRequestDto;
import com.eventorium.data.models.CategoryResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface CategoryService {

    @GET("categories/all")
    Call<List<CategoryResponseDto>> getCategories();

    @PUT("categories/{id}")
    Call<CategoryResponseDto> updateCategory(@Body CategoryRequestDto dto);
}
