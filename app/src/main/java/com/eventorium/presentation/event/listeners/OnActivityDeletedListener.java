package com.eventorium.presentation.event.listeners;

import com.eventorium.data.event.models.Activity;

public interface OnActivityDeletedListener {
    void delete(Activity activity);
}
