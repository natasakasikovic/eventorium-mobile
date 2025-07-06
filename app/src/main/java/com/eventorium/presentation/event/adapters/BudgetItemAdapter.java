package com.eventorium.presentation.event.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Objects;
import java.util.function.Consumer;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemAdapter.BudgetItemViewHolder> {

    private Context context;
    private List<BudgetItem> budgetItems;
    private OnBudgetItemActionListener listener;

    public BudgetItemAdapter(List<BudgetItem> budgetItems, Context context, OnBudgetItemActionListener listener) {
        this.budgetItems = budgetItems;
        this.context = context;
        this.listener = listener;
    }

    public void removeItem(Long itemId) {
        if (itemId == null) return;

        for (int i = 0; i < budgetItems.size(); i++) {
            BudgetItem item = budgetItems.get(i);
            if (itemId.equals(item.getId())) {
                budgetItems.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void updateItem(Long itemId, double spentAmount) {
        for (int i = 0; i < budgetItems.size(); i++) {
            BudgetItem item = budgetItems.get(i);
            if (itemId.equals(item.getId())) {
                item.setStatus(BudgetItemStatus.PROCESSED);
                item.setSpentAmount(spentAmount);
                notifyItemChanged(i);
                return;
            }
        }
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
        EditText plannedAmount;

        TextView statusText;
        ImageView statusIcon;

        MaterialButton reserveButton;
        MaterialButton purchaseButton;
        MaterialButton deleteButton;
        MaterialButton saveButton;

        private TextWatcher textWatcher;
        private final Handler handler = new Handler(Looper.getMainLooper());
        private Runnable debounceRunnable;
        private static final long DEBOUNCE_DELAY = 300;

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

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(BudgetItem item) {
            solutionName.setText(item.getSolutionName());
            categoryName.setText(item.getCategory().getName());
            spentAmount.setText("Spent: " +  String.format("%.2f", item.getSpentAmount()));
            plannedAmount.setText(String.format("%.2f", item.getPlannedAmount()));

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
            resetButtonsAndFields();

            switch (status) {
                case PROCESSED, DENIED -> {
                    hideButtons(purchaseButton, saveButton, reserveButton, deleteButton);
                    disablePlannedAmount();
                }
                case PENDING -> {
                    enablePlannedAmount();
                    enableButton(saveButton, item, listener::onSave);
                    hideButtons(reserveButton, deleteButton, purchaseButton);
                    createPlannedAmountChangeHandler(item);
                }
                case PLANNED -> {
                    enablePlannedAmount();
                    enableButton(deleteButton, item, listener::onDelete);
                    switch (item.getType()) {
                        case PRODUCT -> {
                            enableButton(purchaseButton, item, listener::onPurchase);
                            hideButtons(reserveButton);
                        }
                        case SERVICE -> {
                            enableButton(reserveButton, item, listener::onReserve);
                            hideButtons(purchaseButton);
                        }
                    }
                    createPlannedAmountChangeHandler(item);
                }
            }
        }

        private void hideButtons(MaterialButton ...buttons) {
            for(MaterialButton button : buttons)
                button.setVisibility(View.GONE);
        }

        private void resetButtonsAndFields() {
            reserveButton.setVisibility(View.VISIBLE);
            purchaseButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            reserveButton.setOnClickListener(null);
            purchaseButton.setOnClickListener(null);
            deleteButton.setOnClickListener(null);
            saveButton.setOnClickListener(null);
        }

        private void enablePlannedAmount() {
            plannedAmount.setEnabled(true);
            plannedAmount.setFocusable(true);
            plannedAmount.setFocusableInTouchMode(true);
            plannedAmount.setCursorVisible(true);
            plannedAmount.setBackgroundResource(android.R.drawable.edit_text);
        }
        private void disablePlannedAmount() {
            plannedAmount.setEnabled(false);
            plannedAmount.setFocusable(false);
            plannedAmount.setFocusableInTouchMode(false);
            plannedAmount.setCursorVisible(false);
            plannedAmount.setBackgroundColor(Color.TRANSPARENT);
            plannedAmount.setTextColor(ContextCompat.getColor(context, R.color.status_processed));
        }

        private void createPlannedAmountChangeHandler(BudgetItem item) {
            if (textWatcher != null) {
                plannedAmount.removeTextChangedListener(textWatcher);
            }

            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (debounceRunnable != null)
                        handler.removeCallbacks(debounceRunnable);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().isEmpty()) {
                        debounceRunnable = () -> onPlannedAmountChanged(editable, item);
                        handler.postDelayed(debounceRunnable, DEBOUNCE_DELAY);
                    }
                }
            };

            plannedAmount.addTextChangedListener(textWatcher);
        }

        public void onPlannedAmountChanged(Editable editable, BudgetItem item) {
            try {
                item.setPlannedAmount(Double.parseDouble(editable.toString()));
            } catch (NumberFormatException e) {
                Log.e("PRICE_LIST", Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }

        private void enableButton(MaterialButton button, BudgetItem item, Consumer<BudgetItem> onClick) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> onClick.accept(item));
        }
    }
}
