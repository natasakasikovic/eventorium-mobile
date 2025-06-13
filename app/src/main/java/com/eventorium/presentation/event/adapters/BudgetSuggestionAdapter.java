package com.eventorium.presentation.event.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.BudgetSuggestion;
import com.eventorium.presentation.shared.listeners.OnSeeMoreClick;

import java.util.List;

public class BudgetSuggestionAdapter extends RecyclerView.Adapter<BudgetSuggestionAdapter.BudgetSuggestionViewHolder> {

    private List<BudgetSuggestion> suggestions;
    private OnSeeMoreClick<BudgetSuggestion> listener;

    public BudgetSuggestionAdapter(List<BudgetSuggestion> suggestions, OnSeeMoreClick<BudgetSuggestion> listener) {
        this.suggestions = suggestions;
        this.listener = listener;
    }

    public void setData(List<BudgetSuggestion> items) {
        this.suggestions = items;
        notifyDataSetChanged();
    }

    public int getPosition(BudgetSuggestion suggestion) {
        return suggestions.indexOf(suggestion);
    }

    @NonNull
    @Override
    public BudgetSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_suggestion_card, parent, false);
        return new BudgetSuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetSuggestionViewHolder holder, int position) {
        BudgetSuggestion item = suggestions.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public class BudgetSuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView solutionName;
        TextView solutionPrice;
        TextView solutionDiscount;
        ImageView solutionImage;
        Button seeMoreButton;

        public BudgetSuggestionViewHolder(View itemView) {
            super(itemView);
            solutionName = itemView.findViewById(R.id.solutionName);
            solutionPrice = itemView.findViewById(R.id.solutionPrice);
            solutionDiscount = itemView.findViewById(R.id.solutionDiscount);
            solutionImage = itemView.findViewById(R.id.solutionImage);
            seeMoreButton = itemView.findViewById(R.id.seeMoreButton);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(BudgetSuggestion suggestion) {
            solutionName.setText(suggestion.getName());
            solutionImage.setImageBitmap(suggestion.getImage());
            setDiscount(suggestion);
            solutionPrice.setText(String.format("%.2f", calculateNetPrice(suggestion)));
            seeMoreButton.setOnClickListener(v -> listener.navigateToDetails(suggestion));
        }

        private Double calculateNetPrice(BudgetSuggestion suggestion) {
            return suggestion.getPrice() * (1 - suggestion.getDiscount() / 100);
        }
        @SuppressLint("SetTextI18n")
        private void setDiscount(BudgetSuggestion suggestion){
            if (hasDiscount(suggestion)) {
                solutionDiscount.setVisibility(View.VISIBLE);
                solutionDiscount.setText(suggestion.getDiscount().toString() + "% OFF");
            } else {
                solutionDiscount.setVisibility(View.GONE);
            }
        }

        private boolean hasDiscount(BudgetSuggestion suggestion) {
            return suggestion.getDiscount() != null && suggestion.getDiscount() > 0;
        }
    }
}

