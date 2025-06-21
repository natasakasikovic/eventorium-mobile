package com.eventorium.presentation.event.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eventorium.R;
import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemStatus;
import com.eventorium.presentation.event.listeners.OnBudgetItemActionListener;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {

    private Context context;
    private List<BudgetItem> budgetItems;
    private OnBudgetItemActionListener listener;

    public BudgetItemAdapter(List<BudgetItem> budgetItems, Context context, OnBudgetItemActionListener listener) {
        this.budgetItems = budgetItems;
        this.context = context;
        this.listener = listener;
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

    public class BudgetItemViewHolder extends RecyclerView.ViewHolder {

        TextView solutionName;
        TextView categoryName;
        TextView spentAmount;
        TextView plannedAmount;

        TextView statusText;
        ImageView statusIcon;

        MaterialButton reserveButton;
        MaterialButton purchaseButton;
        MaterialButton deleteButton;
        MaterialButton saveButton;

        public BudgetItemViewHolder(View itemView) {
            super(itemView);
            solutionName = itemView.findViewById(R.id.solutionNameText);
            categoryName = itemView.findViewById(R.id.categoryNameText);
            spentAmount = itemView.findViewById(R.id.spentAmountText);
            plannedAmount = itemView.findViewById(R.id.plannedAmountText);
            statusText = itemView.findViewById(R.id.statusText);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            reserveButton = itemView.findViewById(R.id.reserveButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            purchaseButton = itemView.findViewById(R.id.purchaseButton);
            saveButton = itemView.findViewById(R.id.saveButton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(BudgetItem item) {
            solutionName.setText(item.getSolutionName());
            categoryName.setText(item.getCategory().getName());
            spentAmount.setText("Spent: " + item.getSpentAmount());
            plannedAmount.setText("Planned: " + item.getPlannedAmount());

            int statusColor = getStatusColor(item.getStatus());

            statusIcon.setColorFilter(statusColor);
            statusText.setTextColor(statusColor);
            statusText.setText(item.getStatus().toString());

            setupButtons(item);
        }

        private int getStatusColor(BudgetItemStatus status) {
            return switch (status) {
                case PROCESSED -> ContextCompat.getColor(context, R.color.status_processed);
                case DENIED -> ContextCompat.getColor(context, R.color.status_denied);
                case PLANNED -> ContextCompat.getColor(context, R.color.status_planned);
                case PENDING -> ContextCompat.getColor(context, R.color.status_pending);
            };
        }

        private void setupButtons(BudgetItem item) {
            BudgetItemStatus status = item.getStatus();
            switch (status) {
                case PROCESSED, DENIED -> {}
                case PENDING -> enableButton(saveButton, item, listener::onSave);
                case PLANNED -> {
                    enableButton(deleteButton, item, listener::onDelete);
                    switch (item.getType()) {
                        case PRODUCT -> enableButton(purchaseButton, item, listener::onPurchase);
                        case SERVICE -> enableButton(reserveButton, item, listener::onReserve);
                    }
                }
            }
        }

        private void enableButton(MaterialButton button, BudgetItem item, Consumer<BudgetItem> onClick) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> onClick.accept(item));
        }
    }
}
