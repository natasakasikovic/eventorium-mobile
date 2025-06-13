package com.eventorium.presentation.event.listeners;

import com.eventorium.data.event.models.event.EventSummary;

public interface OnManageEventListener {
    void onDeleteClick(EventSummary item);
    void onSeeMoreClick(EventSummary item);
    void onEditClick(EventSummary item);
    void navigateToBudget(EventSummary item);
}
