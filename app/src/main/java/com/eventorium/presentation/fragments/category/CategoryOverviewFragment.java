package com.eventorium.presentation.fragments.category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Category;
import com.eventorium.databinding.FragmentCategoryOverviewBinding;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.adapters.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;


public class CategoryOverviewFragment extends Fragment {

    private FragmentCategoryOverviewBinding binding;
    private final List<Category> categories = new ArrayList<>();

    public CategoryOverviewFragment() {
    }

    public static CategoryOverviewFragment newInstance(String param1, String param2) {
        return new CategoryOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareCategories();

        binding.categoriesRecycleView.setAdapter(new CategoriesAdapter(categories));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareCategories() {
        categories.add(new Category("Electronics", "Devices and gadgets such as smartphones, laptops, and accessories."));
        categories.add(new Category("Clothing", "Apparel and fashion items for men, women, and children."));
        categories.add(new Category("Food & Beverages", "Groceries, packaged foods, and drinks."));
        categories.add(new Category("Home Appliances", "Appliances and tools for home use, such as refrigerators, washing machines, etc."));
        categories.add(new Category("Health & Wellness", "Products related to fitness, medicine, and personal well-being."));
        categories.add(new Category("Automotive", "Cars, motorcycles, spare parts, and automotive accessories."));
    }

}