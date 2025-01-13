package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.EventType;
import com.eventorium.presentation.util.listeners.OnEditClickListener;

import java.util.List;

public class EventTypesAdapter extends RecyclerView.Adapter<EventTypesAdapter.EventTypeViewHolder> {

    private List<EventType> eventTypes;
    private final OnEditClickListener<EventType> onEditClick;

    public EventTypesAdapter(List<EventType> eventTypes, OnEditClickListener<EventType> onEditClick) {
        this.eventTypes = eventTypes;
        this.onEditClick = onEditClick;
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type_card, parent, false);
        return new EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        EventType eventType = eventTypes.get(position);
        holder.nameTextView.setText(eventType.getName());
        holder.descriptionTextView.setText(eventType.getDescription());
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
        notifyDataSetChanged();
    }

    public void removeEventTypeById(Long id) {
        for (int i = 0; i < eventTypes.size(); i++) {
            if (eventTypes.get(i).getId().equals(id)) {
                eventTypes.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public class EventTypeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        ImageButton editButton;
        ImageButton deleteButton;
        public EventTypeViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.event_type_name);
            descriptionTextView = itemView.findViewById(R.id.description);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(v -> {
                EventType eventType = eventTypes.get(getAdapterPosition());
                if (eventType != null) onEditClick.onEditClick(eventType);
            });

            deleteButton.setOnClickListener(v -> {
                EventType eventType = eventTypes.get(getAdapterPosition());
                if (eventType != null) onEditClick.onDeleteClick(eventType);
            });

        }
    }
}
