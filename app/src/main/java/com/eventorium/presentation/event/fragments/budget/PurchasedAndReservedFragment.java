package com.eventorium.presentation.event.fragments.budget;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentPurchasedAndReservedBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PurchasedAndReservedFragment extends Fragment {

    private FragmentPurchasedAndReservedBinding binding;
    private BudgetViewModel budgetViewModel;
    private ProductsAdapter productsAdapter;
    public static final String ARG_ID = "ARG_EVENT_ID";

    private Long eventId;

    public PurchasedAndReservedFragment() {
    }

    public static PurchasedAndReservedFragment newInstance(Long eventId) {
        PurchasedAndReservedFragment fragment = new PurchasedAndReservedFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getLong(ARG_ID);
        }
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPurchasedAndReservedBinding.inflate(inflater, container, false);
        configureAdapter();
        budgetViewModel.getPurchasedProducts(eventId).observe(getViewLifecycleOwner(), products -> {
            if(products.getError() == null) {
                productsAdapter.setData(products.getData());
            }
        });
        return binding.getRoot();
    }

    private void configureAdapter() {
        productsAdapter = new ProductsAdapter(new ArrayList<>(), product -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_ID, product.getId());
            navController.navigate(R.id.action_budget_to_productDetails, args);
        });
        binding.productsRecycleView.setAdapter(productsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}