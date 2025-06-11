
package com.eventorium.presentation.calendar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.event.CalendarEvent;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;

import java.util.List;

public class CalendarEventsAdapter extends RecyclerView.Adapter<CalendarEventsAdapter.ViewHolder> {

    private List<CalendarEvent> calendarEvents;
    private final OnSeeMoreClick<CalendarEvent> listener;

    public CalendarEventsAdapter(List<CalendarEvent> calendarEvents, OnSeeMoreClick<CalendarEvent> listener) {
        this.calendarEvents = calendarEvents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarEvent calendarEvent = calendarEvents.get(position);
        holder.eventNameTextView.setText(calendarEvent.getName());
        holder.arrowButton.setOnClickListener(v -> listener.navigateToDetails(calendarEvent));
    }

    @Override
    public int getItemCount() {
        return calendarEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventNameTextView;
        public ImageButton arrowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.item_name);
            arrowButton = itemView.findViewById(R.id.arrow_button);
        }
    }
}
