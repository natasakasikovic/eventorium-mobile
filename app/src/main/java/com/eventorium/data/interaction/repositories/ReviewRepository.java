package com.eventorium.data.interaction.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.review.CreateReview;
import com.eventorium.data.interaction.models.review.ManageReview;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.data.interaction.models.review.UpdateReview;
import com.eventorium.data.interaction.services.ReviewService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    private final ReviewService reviewService;

    @Inject
    public ReviewRepository(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public LiveData<Result<Review>> createProductReview(Long id, CreateReview request) {
        MutableLiveData<Result<Review>> result = new MutableLiveData<>();
        reviewService.createProductReview(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Review> call,
                    @NonNull Response<Review> response
            ) {
                if (response.isSuccessful()) {
                    result.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<List<ManageReview>> getPendingReviews() {
        MutableLiveData<List<ManageReview>> liveData = new MutableLiveData<>();
        reviewService.getPendingReviews().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ManageReview>> call,
                    @NonNull Response<List<ManageReview>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ManageReview>> call, @NonNull Throwable t) {
                liveData.postValue(new ArrayList<>());
            }
        });
        return liveData;
    }

    public LiveData<Result<Void>> updateReview(Long id, UpdateReview request) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        reviewService.updateReview(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Review> call,
                    @NonNull Response<Review> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(null));
                } else {
                    result.postValue(Result.error("Failed to update review"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Review> call, @NonNull Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });
        return result;
    }
}
