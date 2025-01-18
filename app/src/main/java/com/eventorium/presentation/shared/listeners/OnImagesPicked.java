package com.eventorium.presentation.shared.listeners;

import android.net.Uri;

import java.util.List;

public interface OnImagesPicked {
    void onImagesPicked(List<Uri> imageUris);
}
