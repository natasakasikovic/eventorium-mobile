package com.eventorium.presentation.event.listeners;

import com.eventorium.data.event.models.PastEvent;

public interface OnPastEventActionListener {
    void onStatisticsClicked(PastEvent event);
    void onPdfExportClicked(PastEvent event);
}
