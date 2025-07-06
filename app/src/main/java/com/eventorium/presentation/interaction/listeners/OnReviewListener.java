package com.eventorium.presentation.interaction.listeners;

import com.eventorium.data.interaction.models.review.SolutionReview;

public interface OnReviewListener {
    void onSeeMoreClick(SolutionReview review);
    void onCommentClick(SolutionReview review);
    void onRateClick(SolutionReview review, Integer rating);
}
