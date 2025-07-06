package com.eventorium.presentation.interaction.listeners;

import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.interaction.models.review.ReviewType;

public interface OnManageCommentListener {
    void navigateToProvider(UserDetails provider);
    void navigateToDetails(ReviewType type, Long objectId);
    void acceptComment(Long id);
    void declineComment(Long id);
}
