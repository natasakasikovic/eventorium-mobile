package com.eventorium.presentation.review.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.databinding.FragmentReviewBinding;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.solution.viewmodels.ProductViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private EventViewModel eventViewModel;
    private BudgetViewModel budgetViewModel;

    public ReviewFragment() {
    }

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        eventViewModel = provider.get(EventViewModel.class);
        budgetViewModel = provider.get(BudgetViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        loadEvents();
        return binding.getRoot();
    }

    private void loadEvents() {
        eventViewModel.getOrganizerEvents().observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {

            } else {
                showError(result.getError());
            }
        });
    }

    private void showError(String error) {
        Toast.makeText(
                requireContext(),
                error,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}