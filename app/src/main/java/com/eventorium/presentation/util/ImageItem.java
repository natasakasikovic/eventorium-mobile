package com.eventorium.presentation.util;

import android.graphics.Bitmap;
import android.net.Uri;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageItem {
    private Bitmap bitmap;
    private Uri uri;

    public ImageItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
