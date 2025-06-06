package com.eventorium.presentation.review.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.interaction.models.rating.CreateRating;
import com.eventorium.data.interaction.models.rating.Rating;
import com.eventorium.data.interaction.repositories.RatingRepository;
import com.eventorium.data.shared.models.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RatingViewModel extends ViewModel {

    private final RatingRepository ratingRepository;

    @Inject
    public RatingViewModel(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public LiveData<Result<Rating>> createProductRating(Long id, Integer rating) {
        return ratingRepository.createProductRating(id, new CreateRating(rating));
    }

    public LiveData<Result<Rating>> createServiceRating(Long id, Integer rating) {
        return ratingRepository.createServiceRating(id, new CreateRating(rating));
    }

    public LiveData<Result<Rating>> createEventRating(Long id, Integer rating) {
        return ratingRepository.createEventRating(id, new CreateRating(rating));
    }
}
