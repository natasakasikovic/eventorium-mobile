package com.eventorium.presentation.review.listeners;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.review.ReviewType;

public interface OnManageCommentListener {
    void navigateToProvider(UserDetails provider);
    void navigateToCommentable(ReviewType type, Long objectId);
    void acceptComment(Long id);
    void declineComment(Long id);
}
