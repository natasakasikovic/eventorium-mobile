package com.eventorium.presentation.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.models.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final List<Category> categories;

    public CategoriesAdapter(List<Category> categories) {
        this.categories = categories;
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

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button editButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            descriptionTextView = itemView.findViewById(R.id.category_description);
            editButton = itemView.findViewById(R.id.editButton);

            editButton.setOnClickListener(v -> {
                Category category = categories.get(getAdapterPosition());

                if (category != null) {
                    final View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_edit_category, null);

                    EditText nameEditText = dialogView.findViewById(R.id.categoryNameEditText);
                    EditText descriptionEditText = dialogView.findViewById(R.id.categoryDescriptionEditText);

                    nameEditText.setText(category.getName());
                    descriptionEditText.setText(category.getDescription());

                    new AlertDialog.Builder(itemView.getContext(), R.style.DialogTheme)
                            .setView(dialogView)
                            .setPositiveButton("Save", (dialog, which) -> {
                                String newName = nameEditText.getText().toString();
                                String newDescription = descriptionEditText.getText().toString();

                                category.setName(newName);
                                category.setDescription(newDescription);

                                nameTextView.setText(newName);
                                descriptionTextView.setText(newDescription);

                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
        }
    }
}
