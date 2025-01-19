package com.eventorium.presentation.category.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eventorium.data.category.models.Category;

import com.eventorium.R;
import com.eventorium.presentation.category.listeners.OnReviewProposalListener;

import java.util.List;

public class CategoryProposalsAdapter extends RecyclerView.Adapter<CategoryProposalsAdapter.CategoryProposalViewHolder>{

    private List<Category> categoriesProposals;
    private final OnReviewProposalListener proposalListener;

    public CategoryProposalsAdapter(List<Category> categoriesProposals, OnReviewProposalListener proposalListener) {
        this.categoriesProposals = categoriesProposals;
        this.proposalListener = proposalListener;
    }

    @NonNull
    @Override
    public CategoryProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_proposal_card, parent, false);
        return new CategoryProposalsAdapter.CategoryProposalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProposalViewHolder holder, int position) {
        Category category = categoriesProposals.get(position);
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
    }

    @Override
    public int getItemCount() { return categoriesProposals.size(); }

    public void setCategories(List<Category> proposals) {
        Log.d("SetCategories", "New proposals size: " + proposals.size());
        categoriesProposals = proposals;
        notifyDataSetChanged();
    }

    public class CategoryProposalViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        Button acceptButton;
        Button updateButton;
        Button declineButton;
        public CategoryProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            descriptionTextView = itemView.findViewById(R.id.category_description);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            updateButton = itemView.findViewById(R.id.updateButton);
            declineButton = itemView.findViewById(R.id.declineButton);

            acceptButton.setOnClickListener(v -> {
                Category category = categoriesProposals.get(getAdapterPosition());
                if (category != null) {
                    proposalListener.acceptCategory(category);
                }
            });

            updateButton.setOnClickListener(v -> {
                Category category = categoriesProposals.get(getAdapterPosition());
                if (category != null) {
                    proposalListener.updateCategory(category);
                }
            });

            declineButton.setOnClickListener(v -> {
                Category category = categoriesProposals.get(getAdapterPosition());
                if (category != null) {
                    proposalListener.declineCategory(category);
                }
            });
        }
    }
}
