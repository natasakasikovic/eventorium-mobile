package com.eventorium.presentation.review.listeners;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.review.Review;
import com.eventorium.data.interaction.models.review.SolutionReview;

public interface OnReviewListener {
    void navigateToProvider(UserDetails provider);
    void navigateToSolution(SolutionReview solution);
    void acceptReview(Long id);
    void declineReview(Long id);
}
