package com.eventorium.presentation.shared.utils;


import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;


public class ImageLoader {
    public void loadImage(
            Long id,
            LiveData<Bitmap> imageLiveData,
            ImageView imageView,
            LifecycleOwner owner
    ) {
        imageView.setImageBitmap(null);
        imageLiveData.observe(owner, new Observer<>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                if (bitmap != null) {
                    Object tag = imageView.getTag();
                    if (tag instanceof Long && tag.equals(id)) {
                        imageView.setImageBitmap(bitmap);
                        imageLiveData.removeObserver(this);
                    }
                }
            }
        });
    }

}