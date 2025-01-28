package com.eventorium.presentation.category.listeners;

import com.eventorium.data.category.models.Category;

public interface OnReviewProposalListener {
    void acceptCategory(Category category);
    void declineCategory(Category category);
    void updateCategory(Category category);

}
