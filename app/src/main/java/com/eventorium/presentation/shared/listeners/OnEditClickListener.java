package com.eventorium.presentation.shared.listeners;

public interface OnEditClickListener<T> {
    void onEditClick(T item);
    void onDeleteClick(T item);
}
