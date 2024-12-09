package com.eventorium.data.category.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.category.mappers.CategoryMapper;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.services.CategoryProposalService;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProposalRepository {

    private final CategoryProposalService service;
    @Inject
    public CategoryProposalRepository(CategoryProposalService service) {
        this.service = service;
    }

    public LiveData<List<Category>> getCategoryProposals() {
        MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        service.getServiceProposals().enqueue(new Callback<>() {
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

}
