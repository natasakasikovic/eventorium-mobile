package com.eventorium.presentation.category.listeners;

public interface OnManualReservationListener {
    void navigateToEvent(Long id);
    void navigateToService(Long id);
    void acceptReservation(Long id);
    void declineReservation(Long id);
}
