package com.eventorium.presentation.util.listeners;

public interface OnManageListener<T> {
    void onDeleteClick(T item);
    void onSeeMoreClick(T item);
    void onEditClick(T item);
}
