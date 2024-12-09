package com.eventorium.presentation.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.eventorium.presentation.util.listeners.OnImagesPicked;

import java.util.ArrayList;
import java.util.List;


public class ImageUpload {
    private ActivityResultLauncher<Intent> launcher;

    public ImageUpload(LifecycleOwner lifecycleOwner, OnImagesPicked callback) {
        if (lifecycleOwner instanceof Fragment fragment) {
            launcher = fragment.registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            List<Uri> imageUris = new ArrayList<>();

                            if (data.getClipData() != null) {
                                int count = data.getClipData().getItemCount();
                                for (int i = 0; i < count; i++) {
                                    imageUris.add(data.getClipData().getItemAt(i).getUri());
                                }
                            } else if (data.getData() != null) {
                                imageUris.add(data.getData());
                            }

                            callback.onImagesPicked(imageUris);
                        }
                    }
            );
        }
    }

    public void openGallery(boolean allowMultiple) {
        if (launcher == null) {
            throw new IllegalStateException("ImageUpload must be initialized before calling openGallery.");
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
        launcher.launch(intent);
    }
}
