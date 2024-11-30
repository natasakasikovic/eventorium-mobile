package com.eventorium.data.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.mappers.CategoryMapper;
import com.eventorium.data.models.Category;
import com.eventorium.data.models.CategoryRequestDto;
import com.eventorium.data.models.CategoryResponseDto;
import com.eventorium.data.services.CategoryService;
import com.eventorium.domain.repositories.CategoryRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryService categoryService;

    public CategoryRepositoryImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }



    @Override
    public LiveData<List<Category>> getCategories() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();

        categoryService.getCategories().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<CategoryResponseDto>> call,
                    @NonNull Response<List<CategoryResponseDto>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body()
                            .stream().map(CategoryMapper::fromResponse)
                            .collect(toList());
                    liveData.postValue(categories);
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<CategoryResponseDto>> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Collections.emptyList());
            }
        });

        return liveData;
    }

    @Override
    public LiveData<Category> updateCategory(Long id, Category category) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();

        categoryService.updateCategory(id, CategoryMapper.toRequest(category)).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<CategoryResponseDto> call,
                    @NonNull Response<CategoryResponseDto> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(CategoryMapper.fromResponse(response.body()));
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<CategoryResponseDto> call,
                    @NonNull Throwable t
            ) {

            }
        });



        return liveData;
    }
}
