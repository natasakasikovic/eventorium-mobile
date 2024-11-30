package com.eventorium.data.repositories;

import static java.util.stream.Collectors.toList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.mappers.CategoryMapper;
import com.eventorium.data.models.Category;
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
}
