package com.eventorium.presentation.favourites.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.solution.models.product.ProductSummary;
import com.eventorium.databinding.FragmentFavouriteProductsBinding;

import java.util.ArrayList;
import java.util.List;

public class FavouriteProductsFragment extends Fragment {

    private FragmentFavouriteProductsBinding binding;
    private static final List<ProductSummary> PRODUCT_SUMMARIES = new ArrayList<>();

    public FavouriteProductsFragment() { }

    public static FavouriteProductsFragment newInstance() {
        return new FavouriteProductsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.productsRecycleView.setAdapter(new ProductsAdapter(PRODUCT_SUMMARIES));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}