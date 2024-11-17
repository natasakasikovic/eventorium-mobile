package com.eventorium.presentation.fragments.service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eventorium.R;
import com.eventorium.data.models.Service;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.adapters.service.ServiceAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ServiceOverviewFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private static final List<Service> services = new ArrayList<>();

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
        prepareServiceData();

        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.servicesRecycleView.setAdapter(new ServiceAdapter(services));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void prepareServiceData(){
        services.clear();
        services.add(new Service("Catering", 500.0, R.drawable.catering));
        services.add(new Service("Photography", 300.0, R.drawable.catering));
        services.add(new Service("Venue Setup", 200.0, R.drawable.catering));
        services.add(new Service("DJ Service", 250.0, R.drawable.catering));
    }

}
