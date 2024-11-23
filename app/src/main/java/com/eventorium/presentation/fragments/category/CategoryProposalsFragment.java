package com.eventorium.presentation.fragments.category;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.models.Category;
import com.eventorium.databinding.FragmentCategoryProposalsBinding;
import com.eventorium.presentation.adapters.category.CategoriesAdapter;
import com.eventorium.presentation.adapters.category.CategoryProposalsAdapter;
import com.eventorium.presentation.util.OnReviewProposalListener;
import com.eventorium.presentation.viewmodels.CategoryProposalViewModel;


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

                }
            });


        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}