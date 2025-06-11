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
import com.eventorium.presentation.solution.listeners.OnManageListener;

import java.util.List;

public class ManageableEventAdapter extends BaseEventAdapter<ManageableEventAdapter.ManageableEventViewHolder> {

    private final OnManageListener<EventSummary> manageListener;

    public ManageableEventAdapter(List<EventSummary> eventSummaries, OnManageListener<EventSummary> listener) {
        super(eventSummaries);
        manageListener = listener;
    }

    @NonNull
    @Override
    public ManageableEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manageable_event_card, parent, false);
        return new ManageableEventViewHolder(view);
    }

    public void setEvents(List<EventSummary> events) {
        eventSummaries = events;
        notifyDataSetChanged();
    }

    public class ManageableEventViewHolder extends BaseEventViewHolder {

        TextView nameTextView;
        TextView cityTextView;

        Button seeMoreButton;
        Button editButton;
        ImageView photoImageView;

        public ManageableEventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_name);
            cityTextView = itemView.findViewById(R.id.event_city);
            seeMoreButton = itemView.findViewById(R.id.see_more_button);
            editButton = itemView.findViewById(R.id.edit_button);
            photoImageView = itemView.findViewById(R.id.event_photo);
        }

        @Override
        public void bind(EventSummary event) {
            nameTextView.setText(event.getName());
            cityTextView.setText(event.getCity());
            seeMoreButton.setOnClickListener(v -> manageListener.onSeeMoreClick(event));
            editButton.setOnClickListener(v -> manageListener.onEditClick(event));
            photoImageView.setImageBitmap(event.getImage());
        }
    }
}