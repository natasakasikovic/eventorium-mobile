package com.eventorium.presentation.event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.Activity;
import com.eventorium.presentation.event.listeners.OnActivityDeletedListener;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ActivityViewHolder> {
    private List<Activity> activities;
    private final OnActivityDeletedListener listener;

    public ActivitiesAdapter(List<Activity> activities, OnActivityDeletedListener listener) {
        this.activities = activities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActivitiesAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ActivityViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.nameTextView.setText(activity.getName());
        holder.descriptionTextView.setText(activity.getDescription());
        holder.startTimeTextView.setText(activity.getStartTime());
        holder.endTimeTextView.setText(activity.getEndTime());
        holder.locationTextView.setText(activity.getLocation());
        holder.deleteBtn.setOnClickListener(v -> {
            activities.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView descriptionTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView locationTextView;
        ImageButton deleteBtn;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
