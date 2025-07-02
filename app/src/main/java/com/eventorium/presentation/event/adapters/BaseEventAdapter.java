package com.eventorium.presentation.event.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.solution.models.service.ServiceSummary;

import java.util.List;

public abstract class BaseEventAdapter<T extends BaseEventAdapter.BaseEventViewHolder> extends RecyclerView.Adapter<T> {

    protected List<EventSummary> eventSummaries;

    public BaseEventAdapter(List<EventSummary> eventSummaries) {
        this.eventSummaries = eventSummaries;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void setData(List<EventSummary> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return eventSummaries.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return eventSummaries.get(oldItemPosition).getId()
                        .equals(newData.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return eventSummaries.get(oldItemPosition)
                        .equals(newData.get(newItemPosition));
            }
        });

        eventSummaries.clear();
        eventSummaries.addAll(newData);

        diffResult.dispatchUpdatesTo(this);
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
