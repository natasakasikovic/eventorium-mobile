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
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.databinding.FragmentPurchasedAndReservedBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;
import com.eventorium.presentation.solution.fragments.product.ProductDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PurchasedAndReservedFragment extends Fragment {

    private FragmentPurchasedAndReservedBinding binding;
    private BudgetViewModel budgetViewModel;
    private ProductsAdapter productsAdapter;
    public static final String ARG_EVENT = "ARG_EVENT";

    private ProductViewModel productViewModel;

    private Event event;

    public PurchasedAndReservedFragment() {
    }

    public static PurchasedAndReservedFragment newInstance(Event event) {
        PurchasedAndReservedFragment fragment = new PurchasedAndReservedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            event = getArguments().getParcelable(ARG_EVENT);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        budgetViewModel = provider.get(BudgetViewModel.class);
        productViewModel = provider.get(ProductViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPurchasedAndReservedBinding.inflate(inflater, container, false);
        configureAdapter();
        return binding.getRoot();
    }

    private void loadProductImages(List<ProductSummary> products) {
        products.forEach( product -> productViewModel.getProductImage(product.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        product.setImage(image);
                        int position = products.indexOf(product);
                        if (position != -1) {
                            productsAdapter.notifyItemChanged(position);
                        }
                    }
                })
        );
    }
    private void configureAdapter() {
        productsAdapter = new ProductsAdapter(new ArrayList<>(), product -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ProductDetailsFragment.ARG_ID, product.getId());
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