package com.eventorium.presentation.event.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.budget.BudgetItem;

import java.util.List;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {

    private List<BudgetItem> budgetItems;

    public BudgetItemAdapter(List<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;
    }

    public void setData(List<BudgetItem> items) {
        this.budgetItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BudgetItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.budget_item_card, parent, false);
        return new BudgetItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemViewHolder holder, int position) {
        BudgetItem item = budgetItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    public static class BudgetItemViewHolder extends RecyclerView.ViewHolder {

        TextView solutionName;
        TextView categoryName;
        TextView spentAmount;
        TextView plannedAmount;

        public BudgetItemViewHolder(View itemView) {
            super(itemView);
            solutionName = itemView.findViewById(R.id.solutionNameText);
            categoryName = itemView.findViewById(R.id.categoryNameText);
            spentAmount = itemView.findViewById(R.id.spentAmountText);
            plannedAmount = itemView.findViewById(R.id.plannedAmountText);
        }

        @SuppressLint("SetTextI18n")
        public void bind(BudgetItem item) {
            solutionName.setText("Solution: " + item.getSolutionName());
            categoryName.setText("Category: " + item.getCategory().getName());
            spentAmount.setText("Spent Amount: " + item.getSpentAmount());
            plannedAmount.setText("Planned Amount: " + item.getPlannedAmount());
        }
    }
}
