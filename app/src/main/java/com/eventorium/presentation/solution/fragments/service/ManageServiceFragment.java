package com.eventorium.presentation.solution.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.solution.adapters.ManageableServiceAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class ManageServiceFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private static final List<ServiceSummary> SERVICE_SUMMARIES = new ArrayList<>();

    public ManageServiceFragment() {
    }
    public static ManageServiceFragment newInstance() {
        return new ManageServiceFragment();
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
        prepareServiceData();

        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.servicesRecycleView.setAdapter(new ManageableServiceAdapter(SERVICE_SUMMARIES));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareServiceData(){
        SERVICE_SUMMARIES.clear();
        SERVICE_SUMMARIES.add(new ServiceSummary("Catering", 500.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Photography", 300.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Venue Setup", 200.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("DJ ServiceSummary", 250.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Florist", 150.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Transportation", 400.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Videography", 350.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Cake Design", 100.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Hair and Makeup", 200.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Security", 300.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Guest Coordination", 250.0, R.drawable.catering));
        SERVICE_SUMMARIES.add(new ServiceSummary("Bar ServiceSummary", 200.0, R.drawable.catering));
    }
}