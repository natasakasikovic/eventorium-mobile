package com.eventorium.presentation.solution.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.solution.models.service.ServiceSummary;

import java.util.List;

public abstract class BaseServiceAdapter<T extends BaseServiceAdapter.BaseServiceViewHolder>
        extends RecyclerView.Adapter<T> {

    protected List<ServiceSummary> serviceSummaries;

    public BaseServiceAdapter(List<ServiceSummary> serviceSummaries) {
        this.serviceSummaries = serviceSummaries;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public void setData(List<ServiceSummary> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return serviceSummaries.size();
            }

            @Override
            public int getNewListSize() {
                return newData.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return serviceSummaries.get(oldItemPosition).getId()
                        .equals(newData.get(newItemPosition).getId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return serviceSummaries.get(oldItemPosition)
                        .equals(newData.get(newItemPosition));
            }
        });

        serviceSummaries.clear();
        serviceSummaries.addAll(newData);

        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        ServiceSummary serviceSummary = serviceSummaries.get(position);
        holder.bind(serviceSummary);
    }

    @Override
    public int getItemCount() {
        return serviceSummaries.size();
    }

    public abstract static class BaseServiceViewHolder extends RecyclerView.ViewHolder {
        public BaseServiceViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(ServiceSummary serviceSummary);
    }
}
