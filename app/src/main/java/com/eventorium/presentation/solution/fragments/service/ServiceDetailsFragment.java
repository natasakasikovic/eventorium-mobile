package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.Event;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.google.android.material.button.MaterialButton;

import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceDetailsFragment extends Fragment {

    private FragmentServiceDetailsBinding binding;
    private ServiceViewModel serviceViewModel;

    private MaterialButton favouriteButton;
    private boolean isFavourite;
    public static final String ARG_ID = "ARG_SERVICE_ID";
    public static final String ARG_PLANNED_AMOUNT = "ARG_PLANNED_AMOUNT";
    public static final String ARG_EVENT = "ARG_EVENT";


    public ServiceDetailsFragment() {
    }

    public static ServiceDetailsFragment newInstance(Long id) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static ServiceDetailsFragment newInstance(
        Long id,
        Event event,
        Double plannedAmount
    ) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putDouble(ARG_PLANNED_AMOUNT, plannedAmount);
        args.putParcelable(ARG_EVENT, event);
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
        favouriteButton = binding.favButton;
        if(!serviceViewModel.isLoggedIn()) {
            favouriteButton.setVisibility(View.GONE);
        }
        serviceViewModel.getService(getArguments().getLong(ARG_ID)).observe(getViewLifecycleOwner(), service -> {
            if(service != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

                binding.serviceName.setText(service.getName());
                binding.servicePrice.setText(service.getPrice().toString());
                binding.serviceDescription.setText(service.getDescription());
                binding.serviceCategory.setText("Category: " + service.getCategory().getName());
                binding.providerName.setText(service.getProvider().getName() + " " + service.getProvider().getLastname());
                binding.companyName.setText(service.getCompany().getName());
                binding.serviceSpecialties.setText(service.getSpecialties());
                binding.duration.setText("Duration:" + (service.getMinDuration().equals(service.getMaxDuration())
                        ? service.getMinDuration() + "h"
                        : service.getMinDuration() + "h -" + service.getMaxDuration() + "h"));
                binding.reservationDeadline.setText("Reservation deadline: " + service.getReservationDeadline().format(formatter));
                binding.cancellationDeadline.setText("Cancellation deadline: " + service.getCancellationDeadline().format(formatter));
                binding.rating.setText(service.getRating().toString());

                serviceViewModel.getServiceImages(service.getId()).observe(getViewLifecycleOwner(), images -> {
                    binding.images.setAdapter(new ImageAdapter(images.stream().map(ImageItem::new).collect(toList())));
                });
            }
        });

        serviceViewModel.isFavourite(getArguments().getLong(ARG_ID)).observe(getViewLifecycleOwner(), result -> {
            isFavourite = result;
            favouriteButton.setIconResource(
                    result
                    ? R.drawable.ic_favourite
                    : R.drawable.ic_not_favourite
            );
        });

        favouriteButton.setOnClickListener(v -> {
            Long id = getArguments().getLong(ARG_ID);
            if(isFavourite) {
                serviceViewModel.removeFavouriteService(id).observe(getViewLifecycleOwner(), result -> {
                    if(result) {
                        isFavourite = false;
                        favouriteButton.setIconResource(R.drawable.ic_not_favourite);
                        Toast.makeText(
                                requireContext(),
                                R.string.removed_service_from_favourites,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            } else {
                serviceViewModel.addFavouriteService(id).observe(getViewLifecycleOwner(), name -> {
                    if(name != null) {
                        isFavourite = true;
                        favouriteButton.setIconResource(R.drawable.ic_favourite);
                        Toast.makeText(
                                requireContext(),
                                getString(R.string.added_service)
                                        + name
                                        + getString(R.string.to_favourites),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
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