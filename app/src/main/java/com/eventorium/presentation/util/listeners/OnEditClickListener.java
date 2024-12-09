package com.eventorium.presentation.util.listeners;

public interface OnEditClickListener<T> {
    void onEditClick(T item);
    void onDeleteClick(T item);
}
