package com.eventorium.data.interaction.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.comment.Comment;
import com.eventorium.data.interaction.models.rating.CreateRating;
import com.eventorium.data.interaction.models.rating.Rating;
import com.eventorium.data.interaction.services.RatingService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRepository {

    private final RatingService ratingService;

    @Inject
    public RatingRepository(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public LiveData<Result<Rating>> createServiceRating(Long id, CreateRating request) {
        MutableLiveData<Result<Rating>> result = new MutableLiveData<>();
        ratingService.createServiceRating(id, request).enqueue(handleRequest(result));
        return result;
    }

    public LiveData<Result<Rating>> createProductRating(Long id, CreateRating request) {
        MutableLiveData<Result<Rating>> result = new MutableLiveData<>();
        ratingService.createProductRating(id, request).enqueue(handleRequest(result));
        return result;
    }

    public LiveData<Result<Rating>> createEventRating(Long id, CreateRating request) {
        MutableLiveData<Result<Rating>> result = new MutableLiveData<>();
        ratingService.createEventRating(id, request).enqueue(handleRequest(result));
        return result;
    }

    private Callback<Rating> handleRequest(MutableLiveData<Result<Rating>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<Rating> call,
                    @NonNull Response<Rating> response
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
            public void onFailure(@NonNull Call<Rating> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

}
