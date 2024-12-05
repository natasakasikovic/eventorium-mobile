package com.eventorium.presentation.category.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.models.Category;
import com.eventorium.presentation.util.listeners.OnEditClickListener;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private final OnEditClickListener<Category> onEditClick;

    public CategoriesAdapter(List<Category> categories, OnEditClickListener<Category> onEditClick) {
        this.categories = categories;
        this.onEditClick = onEditClick;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> newCategories) {
        categories = newCategories;
        notifyDataSetChanged();
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button editButton;
        Button deleteButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            descriptionTextView = itemView.findViewById(R.id.category_description);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(v -> {
                Category category = categories.get(getAdapterPosition());
                if(category != null) {
                    onEditClick.onEditClick(category);
                }
            });

            deleteButton.setOnClickListener(v -> {
                Category category = categories.get(getAdapterPosition());
                if(category != null) {
                    onEditClick.onDeleteClick(category);
                }
            });

        }
    }
}
