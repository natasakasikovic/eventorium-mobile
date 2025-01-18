package com.eventorium.presentation.shared.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.listeners.OnImageDeleteListener;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final List<ImageItem> images;
    private OnImageDeleteListener imageDeleteListener;
    private static final int NON_DELETABLE = 0;
    private static final int DELETABLE = 1;

    public ImageAdapter(List<ImageItem> images) {
        this.images = images;
    }

    public ImageAdapter(List<ImageItem> images, OnImageDeleteListener imageDeleteListener) {
        this.images = images;
        this.imageDeleteListener = imageDeleteListener;
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
        holder.imageView.setImageBitmap(images.get(position).getBitmap());
        if (imageDeleteListener != null && holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                ImageItem item = images.get(position);
                imageDeleteListener.delete(new ImageItem(item.getId(), item.getBitmap(), item.getUri()));
                images.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, images.size());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return imageDeleteListener != null ? DELETABLE : NON_DELETABLE;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void insert(List<ImageItem> images) {
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
            imageView.setPadding(15, 0, 15, 0);
        }

        public ImageViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
            imageView.setPadding(15, 0, 15, 0);
        }

    }
}
