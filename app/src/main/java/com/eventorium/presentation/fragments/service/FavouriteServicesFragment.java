package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Service;
import com.eventorium.databinding.FragmentFavouriteServicesBinding;
import com.eventorium.presentation.adapters.service.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteServicesFragment extends Fragment {

    private FragmentFavouriteServicesBinding binding;
    private static final List<Service> services = new ArrayList<>();

    public FavouriteServicesFragment() { }

    public static FavouriteServicesFragment newInstance() {
        return new FavouriteServicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouriteServicesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareServiceData();

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