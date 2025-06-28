package com.eventorium.data.event.models.budget;

import androidx.annotation.NonNull;

public enum BudgetItemStatus {
    PROCESSED,
    DENIED,
    PLANNED,
    PENDING;

    @Override
    @NonNull
    public String toString() {
        String lowercase = name().toLowerCase();
        return Character.toUpperCase(lowercase.charAt(0)) + lowercase.substring(1);
    }

}
