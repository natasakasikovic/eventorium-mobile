package com.eventorium.presentation.event.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.event.models.EventSummary;

import java.util.List;

public abstract class BaseEventAdapter<T extends BaseEventAdapter.BaseEventViewHolder> extends RecyclerView.Adapter<T> {

    protected List<EventSummary> eventSummaries;

    public BaseEventAdapter(List<EventSummary> eventSummaries) {
        this.eventSummaries = eventSummaries;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void setData(List<EventSummary> data) {
        eventSummaries = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        EventSummary eventSummary = eventSummaries.get(position);
        holder.bind(eventSummary);
    }

    @Override
    public int getItemCount() {
        return eventSummaries.size();
    }

    public abstract static class BaseEventViewHolder extends RecyclerView.ViewHolder {

        public BaseEventViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(EventSummary eventSummary);
    }
}
