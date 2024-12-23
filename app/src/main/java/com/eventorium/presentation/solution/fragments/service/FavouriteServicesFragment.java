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
import com.eventorium.databinding.FragmentFavouriteServicesBinding;
import com.eventorium.presentation.solution.adapters.ServiceAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteServicesFragment extends Fragment {

    private FragmentFavouriteServicesBinding binding;
    private static final List<ServiceSummary> SERVICE_SUMMARIES = new ArrayList<>();

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

        binding.servicesRecycleView.setAdapter(new ServiceAdapter(SERVICE_SUMMARIES));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}