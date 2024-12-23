package com.eventorium.presentation.solution.fragments.service;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.solution.services.ServiceService;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.util.adapters.ImageAdapter;

import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceDetailsFragment extends Fragment {

    private FragmentServiceDetailsBinding binding;
    private ServiceViewModel serviceViewModel;
    public static final String ARG_ID = "ARG_SERVICE_ID";



    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance(Long id) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceDetailsBinding.inflate(inflater, container, false);
        assert getArguments() != null;
        if(!serviceViewModel.isLoggedIn()) {
            binding.favButton.setVisibility(View.GONE);
        }
        serviceViewModel.getService(getArguments().getLong(ARG_ID)).observe(getViewLifecycleOwner(), service -> {
            if(service != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

                binding.serviceName.setText(service.getName());
                binding.servicePrice.setText(service.getPrice().toString());
                binding.serviceDescription.setText(service.getDescription());
                binding.serviceCategory.setText("Category: " + service.getCategory().getName());
                binding.serviceSpecialties.setText(service.getSpecialties());
                binding.duration.setText("Duration:" + (service.getMinDuration().equals(service.getMaxDuration())
                        ? service.getMinDuration() + "h"
                        : service.getMinDuration() + "h -" + service.getMaxDuration() + "h"));
                binding.reservationDeadline.setText("Reservation deadline: " + service.getReservationDeadline().format(formatter));
                binding.cancellationDeadline.setText("Cancellation deadline: " + service.getCancellationDeadline().format(formatter));
                binding.rating.setText(service.getRating().toString());

                serviceViewModel.getServiceImages(service.getId()).observe(getViewLifecycleOwner(), images -> {
                    binding.images.setAdapter(new ImageAdapter(images));
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}