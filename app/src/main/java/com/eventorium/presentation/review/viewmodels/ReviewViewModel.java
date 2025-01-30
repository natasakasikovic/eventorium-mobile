package com.eventorium.presentation.review.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.interaction.models.review.CreateReview;
import com.eventorium.data.interaction.models.review.ManageReview;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.data.interaction.models.review.UpdateReview;
import com.eventorium.data.interaction.repositories.ReviewRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private final ReviewRepository reviewRepository;

    @Inject
    public ReviewViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LiveData<Result<List<ManageReview>>> getPendingReviews() {
        return reviewRepository.getPendingReviews();
    }

    public LiveData<Result<Review>> createProductReview(Long id, CreateReview request) {
        return reviewRepository.createProductReview(id, request);
    }

    public LiveData<Result<Void>> updateReview(Long id, UpdateReview request) {
        return reviewRepository.updateReview(id, request);
    }
}
