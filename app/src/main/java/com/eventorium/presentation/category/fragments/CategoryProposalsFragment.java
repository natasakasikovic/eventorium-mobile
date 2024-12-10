package com.eventorium.presentation.category.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.dtos.CategoryRequestDto;
import com.eventorium.data.category.dtos.CategoryUpdateStatusDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.util.models.Status;
import com.eventorium.databinding.FragmentCategoryProposalsBinding;
import com.eventorium.presentation.category.adapters.CategoryProposalsAdapter;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.util.listeners.OnReviewProposalListener;
import com.eventorium.presentation.category.viewmodels.CategoryProposalViewModel;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryProposalsFragment extends Fragment {

    private FragmentCategoryProposalsBinding binding;
    private CategoryProposalViewModel proposalViewModel;
    private CategoryViewModel categoryViewModel;
    private CircularProgressIndicator loadingIndicator;
    private CategoryProposalsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noCategoriesText;


    public CategoryProposalsFragment() {
    }

    public static CategoryProposalsFragment newInstance() {
        return new CategoryProposalsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        proposalViewModel = provider.get(CategoryProposalViewModel.class);
        categoryViewModel = provider.get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryProposalsBinding.inflate(inflater, container, false);
        loadingIndicator = binding.loadingIndicator;
        noCategoriesText = binding.noCategoriesText;
        adapter = new CategoryProposalsAdapter(new ArrayList<>(), new OnReviewProposalListener() {
            @Override
            public void acceptCategory(Category category) {
                proposalViewModel.updateCategoryStatus(
                        category.getId(),
                        new CategoryUpdateStatusDto(Status.ACCEPTED)
                ).observe(getViewLifecycleOwner(), success
                        -> handleResponse(category.getId(), success));
            }

            @Override
            public void declineCategory(Category category) {
                proposalViewModel.updateCategoryStatus(
                        category.getId(),
                        new CategoryUpdateStatusDto(Status.DECLINED)
                ).observe(getViewLifecycleOwner(), success
                        -> handleResponse(category.getId(), success));
            }

            @Override
            public void updateCategory(Category category) {
                proposalViewModel.setSelectedCategory(category);
                showUpdateDialog(category);
            }
        });
        recyclerView = binding.categoriesRecycleView;
        recyclerView.setAdapter(adapter);

        showLoadingIndicator();
        loadCategoriesProposals();

        return binding.getRoot();
    }

    private void handleResponse(Long id, boolean success) {
        if(success) {
            Toast.makeText(
                    requireContext(),
                    R.string.category_accepted_successfully,
                    Toast.LENGTH_SHORT
            ).show();
            proposalViewModel.refreshProposals(id);
        } else {
            Toast.makeText(
                    requireContext(),
                    R.string.failed_to_accept_category,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadCategoriesProposals() {
        proposalViewModel.getCategoryProposals().observe(getViewLifecycleOwner(), proposals -> {
            if(proposals != null && !proposals.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                noCategoriesText.setVisibility(View.GONE);
                adapter.setCategories(proposals);
            } else {
                adapter.setCategories(Collections.EMPTY_LIST);
                recyclerView.setVisibility(View.GONE);
                noCategoriesText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoadingIndicator() {
        proposalViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
                categoryViewModel.getExistingCategoriesName()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        existingCategoriesSpinner.setAdapter(adapter);
        existingCategoriesSpinner.setTag(categoryViewModel.getCategories().getValue());

        nameEditText.setText(category.getName());
        descriptionEditText.setText(category.getDescription());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), R.style.DialogTheme)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String categoryName = existingCategoriesSpinner.getSelectedItem().toString();
                    if(!categoryName.isEmpty()) {
                        Category selectedCategory = ((List<Category>) existingCategoriesSpinner.getTag())
                                .stream()
                                .filter(c -> c.getName().equals(categoryName))
                                .findFirst().get();
                        proposalViewModel.changeCategory(
                                category.getId(),
                                new CategoryRequestDto(
                                        selectedCategory.getName(),
                                        selectedCategory.getDescription()
                                )
                        ).observe(getViewLifecycleOwner(), success
                                -> handleResponse(category.getId(), success));
                        return;
                    }
                    String newName = nameEditText.getText().toString();
                    String newDescription = descriptionEditText.getText().toString();

                    proposalViewModel.updateCategoryProposal(
                            category.getId(),
                            new CategoryRequestDto(newName, newDescription)
                    ).observe(getViewLifecycleOwner(), success
                            -> handleResponse(category.getId(), success));

                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();


        int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.9);
        Objects.requireNonNull(alertDialog.getWindow())
                .setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}