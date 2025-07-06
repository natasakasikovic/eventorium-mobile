package com.eventorium.presentation.category.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.category.models.CategoryRequest;
import com.eventorium.databinding.FragmentCreateCategoryBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateCategoryFragment extends Fragment {

    private FragmentCreateCategoryBinding binding;
    private CategoryViewModel categoryViewModel;

    private TextInputEditText nameTextEdit;
    private TextInputEditText descriptionTextEdit;

    public CreateCategoryFragment() {
    }

    public static CreateCategoryFragment newInstance() {
        return new CreateCategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false);

        nameTextEdit = binding.categoryNameEditText;
        descriptionTextEdit = binding.categoryDescriptionEditText;
        binding.createCategoryButton.setOnClickListener(v -> createCategory());


        return binding.getRoot();
    }

    private void createCategory() {
        String name = Objects.requireNonNull(nameTextEdit.getText()).toString();
        String description = Objects.requireNonNull(descriptionTextEdit.getText()).toString();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        CategoryRequest dto = new CategoryRequest(name, description);

        categoryViewModel.createCategory(dto).observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                Toast.makeText(
                        requireContext(),
                        R.string.category_created_successfully,
                        Toast.LENGTH_SHORT
                ).show();
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_create_to_categoryOverview, null, new NavOptions.Builder()
                        .setPopUpTo(R.id.createCategoryFragment, true)
                        .build());
            } else {
                Toast.makeText(
                        requireContext(),
                        R.string.failed_to_create_category,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}