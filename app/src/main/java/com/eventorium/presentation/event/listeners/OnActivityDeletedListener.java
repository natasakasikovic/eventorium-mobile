package com.eventorium.presentation.event.listeners;

import com.eventorium.data.event.models.event.Activity;

public interface OnActivityDeletedListener {
    void delete(Activity activity);
}
