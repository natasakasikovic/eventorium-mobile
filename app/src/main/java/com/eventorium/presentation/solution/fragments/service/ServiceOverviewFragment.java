package com.eventorium.presentation.solution.fragments.service;

import static com.eventorium.presentation.solution.fragments.service.ServiceDetailsFragment.ARG_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.solution.models.service.ServiceSummary;
import com.eventorium.databinding.FragmentServiceOverviewBinding;
import com.eventorium.presentation.solution.adapters.ServicesAdapter;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceOverviewFragment extends Fragment {

    private FragmentServiceOverviewBinding binding;
    private ServiceViewModel viewModel;
    private ServicesAdapter adapter;

    public ServiceOverviewFragment() {}

    public static ServiceOverviewFragment newInstance() {
        return new ServiceOverviewFragment();
    }

    private void configureServiceAdapter() {
        adapter = new ServicesAdapter( new ArrayList<>(), service -> {
            NavController navController = Navigation.findNavController( requireActivity(), R.id.fragment_nav_content_main );
            Bundle args = new Bundle();
            args.putLong(ARG_ID, service.getId());
            navController.navigate(R.id.action_serviceOverview_to_service_details, args);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceOverviewBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        configureServiceAdapter();
        binding.servicesRecycleView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeServices();
        setUpListener();
    }

    private void setUpListener(){
        binding.filterButton.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.service_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String keyword) {
                viewModel.searchServices(keyword).observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null)
                        adapter.setData(result.getData());
                    else
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
                });
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void observeServices() {
        viewModel.getServices().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                binding.servicesRecycleView.setVisibility(View.VISIBLE);
                binding.loadingIndicator.setVisibility(View.GONE);
                adapter.setData(result.getData());
                loadServiceImages(result.getData());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadServiceImages(List<ServiceSummary> services) {
        services.forEach( service -> viewModel.getServiceImage(service.getId()).
                observe (getViewLifecycleOwner(), image -> {
                    if (image != null){
                        service.setImage(image);
                        int position = services.indexOf(service);
                        if (position != -1) {
                            adapter.notifyItemChanged(position);
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
