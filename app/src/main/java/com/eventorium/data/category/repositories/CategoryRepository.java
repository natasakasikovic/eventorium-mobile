package com.eventorium.data.category.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.mappers.CategoryMapper;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.category.services.CategoryService;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    private final CategoryService categoryService;

    @Inject
    public CategoryRepository(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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

    public LiveData<Category> updateCategory(Long id, CategoryRequestDto category) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();

        categoryService.updateCategory(id, category).enqueue(new Callback<>() {
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
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<Category> createCategory(CategoryRequestDto dto) {
        MutableLiveData<Category> liveData = new MutableLiveData<>();
        categoryService.createCategory(dto).enqueue(new Callback<>() {
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
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<Boolean> deleteCategory(Long id) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>(true);
        categoryService.deleteCategory(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Void> call,
                    @NonNull Response<Void> response
            ) {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(false);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Void> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                liveData.postValue(false);
            }
        });
        return liveData;
    }
}
