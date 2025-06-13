package com.eventorium.presentation.event.fragments.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentBudgetItemsListBinding;
import com.eventorium.presentation.event.adapters.BudgetItemAdapter;
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
        adapter = new BudgetItemAdapter(new ArrayList<>());
        binding.solutionRecycleView.setAdapter(adapter);
        loadBudgetItems();
        return binding.getRoot();
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