package com.eventorium.presentation.category.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.mappers.CategoryMapper;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.category.repositories.CategoryRepository;
import com.eventorium.data.util.RetrofitApi;
import com.eventorium.databinding.FragmentCategoryOverviewBinding;
import com.eventorium.presentation.category.adapters.CategoriesAdapter;
import com.eventorium.presentation.util.listeners.OnEditClickListener;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.util.viewmodels.ViewModelFactory;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class CategoryOverviewFragment extends Fragment {
    private FragmentCategoryOverviewBinding binding;
    private CategoryViewModel categoryViewModel;
    private CategoriesAdapter adapter;

    private CircularProgressIndicator loadingIndicator;
    private RecyclerView recyclerView;
    private TextView noCategoriesText;

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
        adapter = new CategoriesAdapter(new ArrayList<>(), new OnEditClickListener<>() {
            @Override
            public void onEditClick(Category category) {
                categoryViewModel.setSelectedCategory(category);
                showEditDialog(category);
            }

            @Override
            public void onDeleteClick(Category category) {
                categoryViewModel.setSelectedCategory(category);
                showDeleteDialog(category);
            }
        });

        categoryViewModel = new ViewModelProvider(this,
                new ViewModelFactory(new CategoryRepository(RetrofitApi.categoryService)))
                .get(CategoryViewModel.class);
        recyclerView = binding.categoriesRecycleView;
        recyclerView.setAdapter(adapter);

        loadingIndicator = binding.loadingIndicator;
        noCategoriesText = binding.noCategoriesText;

        loadCategories();
        showLoadingIndicator();
    }

    private void showLoadingIndicator() {
        categoryViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    private void loadCategories() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if(categories != null && !categories.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                noCategoriesText.setVisibility(View.GONE);
                adapter.setCategories(categories);
            } else {
                adapter.setCategories(Collections.EMPTY_LIST);
                recyclerView.setVisibility(View.GONE);
                noCategoriesText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void showDeleteDialog(Category category) {
        new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete " + category.getName() + "?" )
                .setPositiveButton("Delete", (dialog, which) -> {
                    categoryViewModel.deleteCategory(category.getId())
                        .observe(getViewLifecycleOwner(), success -> {
                                if(success) {
                                    Toast.makeText(
                                            requireContext(),
                                            R.string.category_deleted_successfully,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    Toast.makeText(
                                            requireContext(),
                                            R.string.failed_to_delete_category,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                        });
                })
                .setNegativeButton("Cancel", null)
                .show();
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

                    categoryViewModel.updateCategory(category.getId(), CategoryMapper.toRequest(category))
                            .observe(getViewLifecycleOwner(), updatedCategory -> {
                                if(updatedCategory != null) {
                                    Toast.makeText(
                                            requireContext(),
                                            R.string.category_updated_successfully,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    loadCategories();
                                } else {
                                    Toast.makeText(
                                            requireContext(),
                                            R.string.failed_to_update_category,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();


        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow())
                .setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}