package com.eventorium.presentation.shared.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.eventorium.R;
import java.util.function.Supplier;


public class ImageLoader {
    public void loadImage(Supplier<LiveData<Bitmap>> imageSource, ImageView imageView) {
        imageSource.get().observeForever(new Observer<>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageSource.get().removeObserver(this);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }
}