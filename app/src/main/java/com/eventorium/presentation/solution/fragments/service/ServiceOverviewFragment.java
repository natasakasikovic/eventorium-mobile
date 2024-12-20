package com.eventorium.presentation.solution.fragments.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eventorium.R;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ServiceOverviewFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private static final List<ServiceSummary> SERVICE_SUMMARIES = new ArrayList<>();

    public ServiceOverviewFragment() {}

    public static ServiceOverviewFragment newInstance() {
        return new ServiceOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.servicesRecycleView.setAdapter(new ServicesAdapter(SERVICE_SUMMARIES));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
