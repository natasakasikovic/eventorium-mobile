package com.eventorium.presentation.solution.fragments.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.solution.models.ProductSummary;
import com.eventorium.databinding.FragmentProductOverviewBinding;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductOverviewFragment extends Fragment {

    private FragmentProductOverviewBinding binding;
    private static final List<ProductSummary> PRODUCT_SUMMARIES = new ArrayList<>();

    public ProductOverviewFragment() { }

    public static ProductOverviewFragment newInstance() {
        return new ProductOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.productsRecycleView.setAdapter(new ProductsAdapter(PRODUCT_SUMMARIES));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}