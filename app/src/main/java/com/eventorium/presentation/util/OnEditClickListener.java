package com.eventorium.presentation.util;

public interface OnEditClickListener<T> {
    void onEditClick(T item);
    void onDeleteClick(T item);
}
