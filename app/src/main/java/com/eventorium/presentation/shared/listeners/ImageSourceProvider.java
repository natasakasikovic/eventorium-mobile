package com.eventorium.presentation.shared.listeners;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import java.util.function.Supplier;

public interface ImageSourceProvider<T> {
    Supplier<LiveData<Bitmap>> getImageSource(T item);
}
