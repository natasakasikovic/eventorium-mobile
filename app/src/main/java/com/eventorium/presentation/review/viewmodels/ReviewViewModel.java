package com.eventorium.presentation.review.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.interaction.models.review.CreateReview;
import com.eventorium.data.interaction.models.review.ManageReview;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.data.interaction.models.review.UpdateReview;
import com.eventorium.data.interaction.repositories.ReviewRepository;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.models.Status;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@Getter
@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private final ReviewRepository reviewRepository;
    private final MutableLiveData<List<ManageReview>> reviews = new MutableLiveData<>();

    @Inject
    public ReviewViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void getPendingReviews() {
        reviewRepository.getPendingReviews().observeForever(this.reviews::postValue);
    }

    public void removeReview(Long id) {
        reviews.setValue(Objects.requireNonNull(reviews.getValue()).stream()
                .filter(review -> !review.getId().equals(id))
                .collect(toList()));
    }


    public LiveData<Result<Review>> createProductReview(Long id, CreateReview request) {
        return reviewRepository.createProductReview(id, request);
    }

    public LiveData<Result<Void>> updateReview(Long id, Status status) {
        return reviewRepository.updateReview(id, new UpdateReview(status));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        reviewRepository.getPendingReviews().removeObserver(reviews::postValue);
    }
}
