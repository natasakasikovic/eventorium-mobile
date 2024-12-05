package com.eventorium.presentation.solution.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.data.models.Service;

import java.util.List;

public abstract class BaseServiceAdapter<T extends BaseServiceAdapter.BaseServiceViewHolder>
        extends RecyclerView.Adapter<T> {

    protected List<Service> services;

    public BaseServiceAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public abstract T onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public abstract static class BaseServiceViewHolder extends RecyclerView.ViewHolder {
        public BaseServiceViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void bind(Service service);
    }
}
