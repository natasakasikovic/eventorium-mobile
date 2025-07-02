package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eventorium.R;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.shared.models.ImageHolder;
import com.eventorium.presentation.shared.listeners.ImageSourceProvider;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;
import com.eventorium.presentation.shared.utils.ImageLoader;

import java.util.List;

public class EventsAdapter extends BaseEventAdapter<EventsAdapter.EventViewHolder> {

    private final OnSeeMoreClick<EventSummary> listener;
    private ImageLoader imageLoader;
    private ImageSourceProvider<EventSummary> imageSourceProvider;

    public EventsAdapter(
            List<EventSummary> events,
            ImageLoader imageLoader,
            ImageSourceProvider<EventSummary> imageSourceProvider,
            OnSeeMoreClick<EventSummary> listener
    ) {
        super(events);
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.imageSourceProvider = imageSourceProvider;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventSummary event = eventSummaries.get(position);
        holder.bind(event);
    }

    public class EventViewHolder extends BaseEventViewHolder {
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

        @Override
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
