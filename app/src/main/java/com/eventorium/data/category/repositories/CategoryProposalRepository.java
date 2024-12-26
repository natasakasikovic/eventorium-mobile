package com.eventorium.data.category.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryUpdateStatusDto;
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
                Log.e("API_ERROR", "Error: " + t.getLocalizedMessage());
                liveData.postValue(Collections.emptyList());
            }
        });
        return liveData;
    }

    public LiveData<Boolean> updateCategoryStatus(Long id, CategoryUpdateStatusDto categoryUpdateStatusDto) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        service.updateCategoryStatus(id, categoryUpdateStatusDto)
                .enqueue(handleProposalUpdate(liveData));
        return liveData;
    }

    public LiveData<Boolean> updateCategoryProposal(Long id, CategoryRequestDto dto) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        service.updateCategoryProposal(id, dto)
                .enqueue(handleProposalUpdate(liveData));
        return liveData;
    }

    public LiveData<Boolean> changeCategory(Long id, CategoryRequestDto categoryRequestDto) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        service.changeCategoryProposal(id, categoryRequestDto)
                .enqueue(handleProposalUpdate(liveData));
        return liveData;
    }

    private Callback<Category> handleProposalUpdate(MutableLiveData<Boolean> liveData) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Category> call,
                    @NonNull Response<Category> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    liveData.postValue(true);
                } else {
                    liveData.postValue(false);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<Category> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getLocalizedMessage());
                liveData.postValue(false);
            }
        };
    }
}
