package com.eventorium.presentation.solution.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eventorium.R;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.solution.adapters.ManageableServiceAdapter;
import com.eventorium.presentation.solution.viewmodels.ManageableServiceViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageServiceFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private ManageableServiceViewModel manageableServiceViewModel;
    private ServiceViewModel serviceViewModel;
    private ManageableServiceAdapter adapter;

    private CircularProgressIndicator loadingIndicator;
    private RecyclerView recyclerView;
    private TextView noServicesText;

    public ManageServiceFragment() {
    }

    public static ManageServiceFragment newInstance() {
        return new ManageServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        manageableServiceViewModel = provider.get(ManageableServiceViewModel.class);
        serviceViewModel = provider.get(ServiceViewModel.class);
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

        adapter = new ManageableServiceAdapter(new ArrayList<>());
        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });


        recyclerView = binding.servicesRecycleView;
        recyclerView.setAdapter(adapter);
        loadingIndicator = binding.loadingIndicator;
        noServicesText = binding.noServicesText;

        loadServices();
        showLoadingIndicator();
    }

    private void loadServices() {
        manageableServiceViewModel.getManageableServices().observe(getViewLifecycleOwner(), services -> {
            if(services != null && !services.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                noServicesText.setVisibility(View.GONE);

                services.forEach(service ->
                        serviceViewModel.getServiceImage(service.getId())
                        .observe(getViewLifecycleOwner(), image -> {
                            if(image != null) {
                                service.setImage(image);
                                int position = services.indexOf(service);
                                adapter.notifyItemChanged(position);
                            }
                        }));

                adapter.setServices(services);
            } else {
                adapter.setServices(Collections.EMPTY_LIST);
                recyclerView.setVisibility(View.GONE);
                noServicesText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoadingIndicator() {
        manageableServiceViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}