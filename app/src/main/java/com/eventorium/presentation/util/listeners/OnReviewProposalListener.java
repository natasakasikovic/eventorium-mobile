package com.eventorium.presentation.util.listeners;

import com.eventorium.data.models.Category;

public interface OnReviewProposalListener {
    void acceptCategory(Category category);
    void declineCategory(Category category);
    void updateCategory(Category category);

}
