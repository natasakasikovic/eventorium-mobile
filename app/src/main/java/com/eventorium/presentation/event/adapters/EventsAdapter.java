package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.shared.models.ImageHolder;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.Objects;

public class EventsAdapter extends PagedListAdapter<EventSummary, EventsAdapter.EventViewHolder> {

    private final OnSeeMoreClick<EventSummary> listener;
    private final ImageLoader imageLoader;
    private final ImageSourceProvider<EventSummary> imageSourceProvider;

    public EventsAdapter(
            ImageLoader imageLoader,
            ImageSourceProvider<EventSummary> imageSourceProvider,
            OnSeeMoreClick<EventSummary> listener
    ) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventSummary event = getItem(position);
        if (event != null) {
            holder.bind(event);
        }
    }

    public static final DiffUtil.ItemCallback<EventSummary> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull EventSummary oldItem, @NonNull EventSummary newItem) {
                    return Objects.equals(oldItem.getId(), newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull EventSummary oldItem, @NonNull EventSummary newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView cityTextView;
        Button seeMoreButton;
        ImageView photoImageView;

        public EventViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            cityTextView = itemView.findViewById(R.id.event_city);
            photoImageView = itemView.findViewById(R.id.event_photo);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
        }

        public void bind(EventSummary event) {
            nameTextView.setText(event.getName());
            cityTextView.setText(event.getCity());
            seeMoreButton.setOnClickListener(v -> listener.navigateToDetails(event));

            imageLoader.loadImage(
                    ImageHolder.EVENT,
                    event.getId(),
                    imageSourceProvider.getImageSource(event),
                    photoImageView
            );
        }
    }
}

