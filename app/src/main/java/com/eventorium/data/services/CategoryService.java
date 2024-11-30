package com.eventorium.data.services;

import com.eventorium.data.models.CategoryResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

    @GET("categories/all")
    Call<List<CategoryResponseDto>> getCategories();

}
