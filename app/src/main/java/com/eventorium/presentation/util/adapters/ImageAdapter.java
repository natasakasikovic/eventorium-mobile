package com.eventorium.presentation.util.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.Eventorium;
import com.eventorium.R;

import java.io.IOException;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<Bitmap> images;
    private static final int NON_DELETABLE = 0;
    private static final int DELETABLE = 1;

    private boolean isDeletable = false;

    public ImageAdapter(List<Bitmap> images) {
        this.images = images;
    }

    public ImageAdapter(List<Bitmap> images, boolean isDeletable) {
        this.images = images;
        this.isDeletable = isDeletable;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == DELETABLE) {
            View view = inflater.inflate(R.layout.image_with_delete, parent, false);
            return new ImageViewHolder(view);
        } else {
            return new ImageViewHolder(new ImageView(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageBitmap(images.get(position));
        if (isDeletable && holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, images.size());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isDeletable ? DELETABLE : NON_DELETABLE;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void insert(List<Bitmap> images) {
        this.images.addAll(images);
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public ImageViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }

    }
}
