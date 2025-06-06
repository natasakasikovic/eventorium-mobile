package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.company.fragments.CompanyDetailsFragment;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.user.fragments.UserProfileFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ServiceDetailsFragment extends Fragment {

    private FragmentServiceDetailsBinding binding;
    private ServiceViewModel serviceViewModel;

    private MaterialButton favouriteButton;
    private boolean isFavourite;
    public static final String ARG_ID = "ARG_SERVICE_ID";
    private Long serviceId;
    private UserDetails provider;
    private Long companyId;


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
        if(getArguments() != null) {
            serviceId = getArguments().getLong(ARG_ID);
        }
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceDetailsBinding.inflate(inflater, container, false);
        favouriteButton = binding.favButton;
        if (!serviceViewModel.isLoggedIn()) {
            favouriteButton.setVisibility(View.GONE);
        } else {
            setupFavouriteListeners();
        }
        serviceViewModel.getService(serviceId).observe(getViewLifecycleOwner(), service -> {
            if (service != null) {
                displayServiceDate(service);
            }
        });

        favouriteButton.setOnClickListener(v -> handleIsFavourite());
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        binding.providerButton.setOnClickListener(v -> navigateToProvider());
        binding.companyButton.setOnClickListener(v -> navigateToCompany());
        return binding.getRoot();
    }

    private void setupFavouriteListeners() {
        serviceViewModel.isFavourite(serviceId).observe(getViewLifecycleOwner(), result -> {
            isFavourite = result;
            favouriteButton.setIconResource(
                    result
                            ? R.drawable.ic_favourite
                            : R.drawable.ic_not_favourite
            );
        });
    }

    private void handleIsFavourite() {
        if (isFavourite) {
            serviceViewModel.removeFavouriteService(serviceId).observe(getViewLifecycleOwner(), result -> {
                if (result) {
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
            serviceViewModel.addFavouriteService(serviceId).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() == null) {
                    isFavourite = true;
                    favouriteButton.setIconResource(R.drawable.ic_favourite);
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.added_product)
                                    + binding.serviceName.getText() + " "
                                    + getString(R.string.to_favourites),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            requireContext(),
                            result.getError(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayServiceDate(Service service) {
        binding.serviceName.setText(service.getName());
        binding.servicePrice.setText(service.getPrice().toString());
        binding.serviceDescription.setText(service.getDescription());
        binding.serviceCategory.setText("Category: " + service.getCategory().getName());
        binding.serviceSpecialties.setText(service.getSpecialties());
        binding.duration.setText("Duration:" + (service.getMinDuration().equals(service.getMaxDuration())
                ? service.getMinDuration() + "h"
                : service.getMinDuration() + "h -" + service.getMaxDuration() + "h"));
        binding.reservationDeadline.setText("Reservation deadline: " + service.getReservationDeadline() + " days");
        binding.cancellationDeadline.setText("Cancellation deadline: " + service.getCancellationDeadline() + " days");
        binding.rating.setText(service.getRating().toString());
        provider = service.getProvider();
        companyId = service.getCompany().getId();
        binding.providerName.setText(provider.getName() + " " + provider.getLastname());
        binding.companyName.setText(service.getCompany().getName());

        serviceViewModel.getServiceImages(service.getId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                List<ImageItem> images = result.getData();
                if (images.isEmpty()) this.binding.images.setVisibility(View.GONE);
                else binding.images.setAdapter(new ImageAdapter(images));
            }
        });
    }

    private void navigateToChat() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putParcelable(ChatFragment.ARG_RECIPIENT, provider);
        navController.navigate(R.id.chatFragment, args);
    }

    private void navigateToCompany() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(CompanyDetailsFragment.ARG_COMPANY_ID, companyId);
        navController.navigate(R.id.action_serviceDetails_to_company, args);
    }

    private void navigateToProvider() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(UserProfileFragment.ARG_ID, provider.getId());
        navController.navigate(R.id.action_serviceDetails_to_provider, args);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}