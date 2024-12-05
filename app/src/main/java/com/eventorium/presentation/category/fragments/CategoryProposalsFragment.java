package com.eventorium.presentation.category.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.eventorium.R;
import com.eventorium.data.models.Category;
import com.eventorium.databinding.FragmentCategoryProposalsBinding;
import com.eventorium.presentation.category.adapters.CategoryProposalsAdapter;
import com.eventorium.presentation.util.listeners.OnReviewProposalListener;
import com.eventorium.presentation.category.viewmodels.CategoryProposalViewModel;

import java.util.Objects;


public class CategoryProposalsFragment extends Fragment {

    private FragmentCategoryProposalsBinding binding;
    private CategoryProposalViewModel proposalViewModel;

    public CategoryProposalsFragment() {
    }

    public static CategoryProposalsFragment newInstance() {
        return new CategoryProposalsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryProposalsBinding.inflate(inflater, container, false);
        proposalViewModel = new ViewModelProvider(this).get(CategoryProposalViewModel.class);
        setupRecycleView();

        return binding.getRoot();
    }

    private void setupRecycleView() {

        proposalViewModel.getCategoryProposals().observe(getViewLifecycleOwner(), categories -> {
            CategoryProposalsAdapter adapter = new CategoryProposalsAdapter(categories, new OnReviewProposalListener() {
                @Override
                public void acceptCategory(Category category) {

                }

                @Override
                public void declineCategory(Category category) {

                }

                @Override
                public void updateCategory(Category category) {
                    proposalViewModel.setSelectedCategory(category);
                    showUpdateDialog(category);
                }
            });
            binding.categoriesRecycleView.setAdapter(adapter);
        });
    }

    private void showUpdateDialog(Category category) {
        final View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_category_proposal_update, null);

        EditText nameEditText = dialogView.findViewById(R.id.categoryNameEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.categoryDescriptionEditText);
        Spinner existingCategoriesSpinner = dialogView.findViewById(R.id.existingCategories);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                dialogView.getContext(),
                android.R.layout.simple_spinner_item,
                proposalViewModel.getExistingCategoriesName()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        existingCategoriesSpinner.setAdapter(adapter);

        nameEditText.setText(category.getName());
        descriptionEditText.setText(category.getDescription());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = nameEditText.getText().toString();
                    String newDescription = descriptionEditText.getText().toString();

                    category.setName(newName);
                    category.setDescription(newDescription);

                    proposalViewModel.updateCategory(category);
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();


        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow())
                .setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void setupExistingCategorySpinner() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}