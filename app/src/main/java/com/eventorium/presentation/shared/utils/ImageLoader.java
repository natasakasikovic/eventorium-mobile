package com.eventorium.presentation.shared.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;


import com.bumptech.glide.Glide;
import com.eventorium.R;
import com.eventorium.data.shared.models.ImageHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ImageLoader {

    private final Context context;

    private final Map<ImageHolder, LruCache<Long, Bitmap>> cacheMap = new HashMap<>();

    private static final int DEFAULT_CACHE_KB = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;

    public ImageLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public void loadImage(ImageHolder type, Long id, Supplier<LiveData<Bitmap>> imageSource, ImageView imageView) {
        LruCache<Long, Bitmap> cache = getOrCreateCache(type);

        Bitmap cached = cache.get(id);
        if (cached != null) {
            Glide.with(context)
                    .load(cached)
                    .error(R.drawable.ic_block)
                    .centerCrop()
                    .into(imageView);
            return;
        }

        imageSource.get().observeForever(new Observer<>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                imageSource.get().removeObserver(this);
                if (bitmap != null) {
                    cache.put(id, bitmap);
                    Glide.with(context)
                            .load(bitmap)
                            .error(R.drawable.ic_block)
                            .centerCrop()
                            .into(imageView);
                }
            }
        });
    }

    public void clearAllCache() {
        for (LruCache<Long, Bitmap> cache : cacheMap.values()) {
            cache.evictAll();
        }
    }

    private LruCache<Long, Bitmap> getOrCreateCache(ImageHolder type) {
        return cacheMap.computeIfAbsent(type, key -> new LruCache<>(DEFAULT_CACHE_KB) {
            @Override
            protected int sizeOf(Long key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        });
    }
}