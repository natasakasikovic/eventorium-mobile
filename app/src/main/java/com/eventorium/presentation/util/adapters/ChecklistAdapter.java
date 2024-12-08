package com.eventorium.presentation.util.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChecklistAdapter<T> extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private final List<T> items;
    private final List<Boolean> selected = new ArrayList<>();

    public ChecklistAdapter(List<T> items) {
        this.items = items;
        for (int i = 0; i < items.size(); i++) {
            selected.add(false); 
        }
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
        T item = items.get(position);
        holder.itemText.setText(item.toString());
        holder.checkBox.setChecked(selected.get(position));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked)
                -> selected.set(position, isChecked));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<T> getSelectedItems() {
        return IntStream.range(0, items.size())
                .filter(selected::get)
                .mapToObj(items::get)
                .collect(Collectors.toList());
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
