package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Service;
import com.eventorium.databinding.FragmentServiceListBinding;
import com.eventorium.presentation.adapters.ServiceAdapter;
import com.eventorium.presentation.adapters.service.ManageableServiceAdapter;

import java.util.ArrayList;
import java.util.List;


public class ServiceListFragment extends Fragment {

    private FragmentServiceListBinding binding;
    private static final List<Service> services = new ArrayList<>();
    private boolean isManageable;
    private static final String MANAGEABLE_PARAM = "manageable";

    public ServiceListFragment() {
    }
    public static ServiceListFragment newInstance(boolean isManageable) {
        ServiceListFragment fragment = new ServiceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isManageable = getArguments() != null && getArguments().getBoolean(MANAGEABLE_PARAM);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceListBinding.inflate(
                inflater,
                container,
                isManageable
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareServiceData();

        attachSnapHelpers();
        binding.servicesRecycleView.setAdapter(new ManageableServiceAdapter(services));
    }

    public void attachSnapHelpers() {
        SnapHelper snapHelperServices = new LinearSnapHelper();
        snapHelperServices.attachToRecyclerView(binding.servicesRecycleView);
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
        services.add(new Service("Florist", 150.0, R.drawable.catering));
        services.add(new Service("Transportation", 400.0, R.drawable.catering));
        services.add(new Service("Videography", 350.0, R.drawable.catering));
        services.add(new Service("Cake Design", 100.0, R.drawable.catering));
        services.add(new Service("Hair and Makeup", 200.0, R.drawable.catering));
        services.add(new Service("Security", 300.0, R.drawable.catering));
        services.add(new Service("Guest Coordination", 250.0, R.drawable.catering));
        services.add(new Service("Bar Service", 200.0, R.drawable.catering));
    }
}