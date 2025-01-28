package com.eventorium.presentation.util.listeners;

public interface OnPurchaseListener<T> {
    void navigateToDetails(T item);
    void purchase(T item);
}
