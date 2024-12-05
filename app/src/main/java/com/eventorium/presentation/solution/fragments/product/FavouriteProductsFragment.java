package com.eventorium.presentation.solution.fragments.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Product;
import com.eventorium.databinding.FragmentFavouriteProductsBinding;
import com.eventorium.presentation.solution.adapters.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteProductsFragment extends Fragment {

    private FragmentFavouriteProductsBinding binding;
    private static final List<Product> products = new ArrayList<>();

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
        prepareProductData();
        binding.productsRecycleView.setAdapter(new ProductsAdapter(products));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareProductData() {
        products.clear();
        products.add(new Product("Balloon Bouquet", 500.0, R.drawable.baloons));
        products.add(new Product("Confetti Cannon", 200.0, R.drawable.conference));
        products.add(new Product("LED String Lights", 100.0, R.drawable.conference));
        products.add(new Product("Photo Booth Props", 20.0, R.drawable.baloons));
        products.add(new Product("Custom Banner", 25.0, R.drawable.conference));
    }
}