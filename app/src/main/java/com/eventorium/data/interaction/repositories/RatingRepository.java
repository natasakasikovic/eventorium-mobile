package com.eventorium.data.interaction.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.interaction.models.rating.CreateRating;
import com.eventorium.data.interaction.models.rating.Rating;
import com.eventorium.data.interaction.services.RatingService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

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
        ratingService.createServiceRating(id, request).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Rating>> createProductRating(Long id, CreateRating request) {
        MutableLiveData<Result<Rating>> result = new MutableLiveData<>();
        ratingService.createProductRating(id, request).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Rating>> createEventRating(Long id, CreateRating request) {
        MutableLiveData<Result<Rating>> result = new MutableLiveData<>();
        ratingService.createEventRating(id, request).enqueue(handleValidationResponse(result));
        return result;
    }
}
