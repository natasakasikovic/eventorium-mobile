package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentBudgetPlanningBinding;

public class BudgetPlanningFragment extends Fragment {

    private FragmentBudgetPlanningBinding binding;

    public BudgetPlanningFragment() {
    }

    public static BudgetPlanningFragment newInstance(String param1, String param2) {
        return new BudgetPlanningFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetPlanningBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}