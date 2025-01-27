package com.eventorium.data.category.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.data.category.services.CategoryService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
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
                    @NonNull Call<List<Category>> call,
                    @NonNull Response<List<Category>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Category>> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Collections.emptyList());
            }
        });

        return liveData;
    }

    public LiveData<Result<Category>> updateCategory(Long id, CategoryRequest request) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();

        categoryService.updateCategory(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Category> call,
                    @NonNull Response<Category> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    liveData.postValue(Result.error(response.message()));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Category> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Result<Category>> createCategory(CategoryRequest request) {
        MutableLiveData<Result<Category>> liveData = new MutableLiveData<>();
        categoryService.createCategory(request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Category> call,
                    @NonNull Response<Category> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    liveData.postValue(Result.error(response.message()));
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Category> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Result<Void>> deleteCategory(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        categoryService.deleteCategory(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Void> call,
                    @NonNull Response<Void> response
            ) {
                if (response.isSuccessful()) {
                    liveData.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Void> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }
}
