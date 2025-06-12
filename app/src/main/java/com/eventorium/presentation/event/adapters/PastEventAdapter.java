package com.eventorium.presentation.event.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.PastEvent;
import com.eventorium.presentation.event.listeners.OnPastEventActionListener;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PastEventAdapter extends RecyclerView.Adapter<PastEventAdapter.PastEventViewHolder> {

    private List<PastEvent> events;
    private final OnPastEventActionListener listener;

    public PastEventAdapter(List<PastEvent> events, OnPastEventActionListener listener) {
        this.events = events;
        this.listener = listener;
    }

    public void setEvents(List<PastEvent> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PastEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_table_card, parent, false);
        return new PastEventViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PastEventViewHolder holder, int position) {
        PastEvent event = events.get(position);
        holder.nameTextView.setText(event.getName());
        holder.dateTextView.setText("Date: " + event.getDate().toString());
        holder.privacyTextView.setText(event.getPrivacy());
        holder.maxParticipantsTextView.setText("Max participants: " + event.getMaxParticipants().toString());
        holder.cityTextView.setText(event.getCity().getName());
        holder.statsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStatisticsClicked(event);
            }
        });

        holder.exportButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPdfExportClicked(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class PastEventViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView dateTextView;
        TextView privacyTextView;
        TextView maxParticipantsTextView;
        TextView cityTextView;
        MaterialButton statsButton;
        MaterialButton exportButton;

        public PastEventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvEventName);
            dateTextView = itemView.findViewById(R.id.tvEventDate);
            privacyTextView = itemView.findViewById(R.id.tvPrivacy);
            maxParticipantsTextView = itemView.findViewById(R.id.tvMaxParticipants);
            cityTextView = itemView.findViewById(R.id.tvCity);
            statsButton = itemView.findViewById(R.id.btnStats);
            exportButton = itemView.findViewById(R.id.btnExportPdf);
        }
    }
}
