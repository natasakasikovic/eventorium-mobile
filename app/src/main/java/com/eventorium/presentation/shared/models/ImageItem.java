package com.eventorium.presentation.shared.models;

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
    private Long id;
    private Bitmap bitmap;
    private Uri uri;

    public ImageItem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageItem(Bitmap bitmap, Uri uri) {
        this.bitmap = bitmap;
        this.uri = uri;
    }

    public ImageItem(Long id, Bitmap bitmap) {
        this.id = id;
        this.bitmap = bitmap;
    }
}
