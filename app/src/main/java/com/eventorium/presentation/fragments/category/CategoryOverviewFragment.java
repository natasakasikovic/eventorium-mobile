package com.eventorium.presentation.fragments.category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.eventorium.R;
import com.eventorium.data.models.Category;
import com.eventorium.data.repositories.CategoryRepositoryImpl;
import com.eventorium.data.util.RetrofitApi;
import com.eventorium.databinding.FragmentCategoryOverviewBinding;
import com.eventorium.presentation.adapters.category.CategoriesAdapter;
import com.eventorium.presentation.viewmodels.CategoryViewModel;
import com.eventorium.presentation.viewmodels.ViewModelFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


public class CategoryOverviewFragment extends Fragment {

    private FragmentCategoryOverviewBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoriesAdapter adapter;

    public CategoryOverviewFragment() {
    }

    public static CategoryOverviewFragment newInstance() {
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
        adapter = new CategoriesAdapter(new ArrayList<>(), category -> {
            categoryViewModel.setSelectedCategory(category);
            showEditDialog(category);
        });

        categoryViewModel = new ViewModelProvider(this,
                new ViewModelFactory(new CategoryRepositoryImpl(RetrofitApi.categoryService)))
                .get(CategoryViewModel.class);
        binding.categoriesRecycleView.setAdapter(adapter);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if(categories != null && !categories.isEmpty()) {
                adapter.setCategories(categories);
            } else {
                adapter.setCategories(Collections.EMPTY_LIST);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void showEditDialog(Category category) {
        final View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_edit_category, null);

        EditText nameEditText = dialogView.findViewById(R.id.categoryNameEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.categoryDescriptionEditText);

        nameEditText.setText(category.getName());
        descriptionEditText.setText(category.getDescription());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameEditText.getText().toString();
                    String newDescription = descriptionEditText.getText().toString();

                    category.setName(newName);
                    category.setDescription(newDescription);

                    categoryViewModel.updateCategory(category);
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();


        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow())
                .setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}