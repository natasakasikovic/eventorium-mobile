package com.eventorium.presentation.util.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
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


    public void selectItem(String name) {
        OptionalInt selectedItemIndex =
                IntStream.range(0, items.size())
                        .filter(i -> {
                            try {
                                Field nameField = items.get(i).getClass().getDeclaredField("name");
                                nameField.setAccessible(true);
                                return name.equals(nameField.get(items.get(i)));
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .findFirst();

        selectedItemIndex.ifPresent(i -> {
            selected.set(i, true);
            notifyItemChanged(i);
        });
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

        Typeface typeface = ResourcesCompat.getFont(holder.itemText.getContext(), R.font.jejumyeongjo);
        holder.itemText.setTypeface(typeface);

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
