package com.eventorium.presentation.review.listeners;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.comment.Commentable;
import com.eventorium.data.interaction.models.review.ReviewType;

public interface OnReviewListener {
    void navigateToProvider(UserDetails provider);
    void navigateToCommentable(ReviewType type, Commentable commentable);
    void acceptReview(Long id);
    void declineReview(Long id);
}
