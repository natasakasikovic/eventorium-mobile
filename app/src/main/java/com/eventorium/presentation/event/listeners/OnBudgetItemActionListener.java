package com.eventorium.presentation.event.listeners;


import com.eventorium.data.event.models.budget.BudgetItem;

public interface OnBudgetItemActionListener {
    void onReserve(BudgetItem item);
    void onSave(BudgetItem item);
    void onPurchase(BudgetItem item);
    void onDelete(BudgetItem item);
}
