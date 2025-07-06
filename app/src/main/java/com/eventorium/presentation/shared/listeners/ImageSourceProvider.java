package com.eventorium.presentation.shared.listeners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

public interface ImageSourceProvider<T> {
    LiveData<Bitmap> getImageSource(T item);
}
