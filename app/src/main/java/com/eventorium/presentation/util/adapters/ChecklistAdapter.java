package com.eventorium.presentation.util.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;

import java.util.List;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private final List<String> items;

    public ChecklistAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_list, parent, false);
        return new ChecklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistViewHolder holder, int position) {
        String item = items.get(position);
        holder.itemText.setText(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ChecklistViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView itemText;

        ChecklistViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
            itemText = view.findViewById(R.id.itemText);
        }
    }
}
