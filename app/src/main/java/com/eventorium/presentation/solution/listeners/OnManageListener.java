package com.eventorium.presentation.solution.listeners;

public interface OnManageListener<T> {
    void onDeleteClick(T item);
    void onSeeMoreClick(T item);
    void onEditClick(T item);
}
