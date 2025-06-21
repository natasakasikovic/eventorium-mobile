package com.eventorium.presentation.event.fragments.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.event.models.budget.BudgetItem;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.event.models.budget.UpdateBudgetItem;
import com.eventorium.data.solution.models.product.Product;
import com.eventorium.databinding.FragmentBudgetItemsListBinding;
import com.eventorium.presentation.event.adapters.BudgetItemAdapter;
import com.eventorium.presentation.event.listeners.OnBudgetItemActionListener;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BudgetItemsListFragment extends Fragment {

    private FragmentBudgetItemsListBinding binding;
    private BudgetItemAdapter adapter;
    private BudgetViewModel budgetViewModel;
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private Long eventId;

    public BudgetItemsListFragment() {
    }

    public static BudgetItemsListFragment newInstance(Long eventId) {
        BudgetItemsListFragment fragment = new BudgetItemsListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_EVENT_ID);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        budgetViewModel = provider.get(BudgetViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetItemsListBinding.inflate(inflater, container, false);
        setupAdapter();
        binding.solutionRecycleView.setAdapter(adapter);
        loadBudgetItems();
        return binding.getRoot();
    }

    private void setupAdapter() {
        adapter = new BudgetItemAdapter(new ArrayList<>(), requireContext(), new OnBudgetItemActionListener() {
            @Override
            public void onReserve(BudgetItem item) {
                // TODO: have to wait for service reservation pull request
            }

            @Override
            public void onSave(BudgetItem item) {
                UpdateBudgetItem request = new UpdateBudgetItem(item.getPlannedAmount());
                budgetViewModel.updateBudgeItem(eventId, item.getId(), request).observe(getViewLifecycleOwner(), result -> {
                    if(result.getError() == null)
                        handleResponse("Item has been updated successfully!");
                    else
                        handleResponse(result.getError());
                });
            }

            @Override
            public void onPurchase(BudgetItem item) {
                BudgetItemRequest request = BudgetItemRequest.builder()
                        .itemId(item.getId())
                        .category(item.getCategory())
                        .itemType(item.getType())
                        .plannedAmount(item.getPlannedAmount())
                        .build();

                budgetViewModel.purchaseProduct(eventId, request).observe(getViewLifecycleOwner(), result -> {
                    if(result.getError() == null) {
                        handleResponse("Item has been purchased successfully!");
                        Product product = result.getData();
                        double spentAmount = product.getPrice() * (1 - product.getDiscount() / 100);
                        adapter.updateItem(item.getId(), spentAmount);
                    } else handleResponse(result.getError());
                });
            }

            @Override
            public void onDelete(BudgetItem item) {
                budgetViewModel.deleteBudgetItem(eventId, item.getId()).observe(getViewLifecycleOwner(), result -> {
                    if(result.getError() == null) {
                        handleResponse("Item has been deleted successfully!");
                        adapter.removeItem(item.getId());
                    }
                    else
                        handleResponse(result.getError());
                });
            }
        });
    }

    private void handleResponse(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    private void loadBudgetItems() {
        budgetViewModel.getBudgetItems(eventId).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null)
                adapter.setData(result.getData());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}