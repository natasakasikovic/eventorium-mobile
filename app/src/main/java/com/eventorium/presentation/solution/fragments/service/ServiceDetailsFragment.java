package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.util.adapters.ImageAdapter;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.BitSet;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceDetailsFragment extends Fragment {

    private FragmentServiceDetailsBinding binding;
    private ServiceViewModel serviceViewModel;
    public static final String ARG_SERVICE = "serviceSummary";

    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance() {
        return new ServiceDetailsFragment();
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
        serviceViewModel.getService(12L).observe(getViewLifecycleOwner(), service -> {
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