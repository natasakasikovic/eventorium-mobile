package com.eventorium.presentation.solution.fragments.service;

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
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.eventorium.presentation.interaction.fragments.chat.ChatFragment;
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
    private LoginViewModel loginViewModel;
    private MaterialButton favouriteButton;
    private boolean isFavourite;
    public static final String ARG_ID = "ARG_SERVICE_ID";
    public static final String ARG_PLANNED_AMOUNT = "ARG_PLANNED_AMOUNT";
    public static final String ARG_EVENT = "ARG_EVENT";

    private Long id;
    private Double plannedAmount;
    private Event event;
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
        if(getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
            plannedAmount = getArguments().getDouble(ARG_PLANNED_AMOUNT);
            event = getArguments().getParcelable(ARG_EVENT);
        }

        ViewModelProvider provider = new ViewModelProvider(this);
        serviceViewModel = provider.get(ServiceViewModel.class);
        loginViewModel = provider.get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceDetailsBinding.inflate(inflater, container, false);
        favouriteButton = binding.favButton;
        if(!loginViewModel.isLoggedIn()) {
            favouriteButton.setVisibility(View.GONE);
        } else {
            setupFavouriteListeners();
        }
        serviceViewModel.getService(id).observe(getViewLifecycleOwner(), service -> {
            if(service != null) {
                displayServiceDate(service);
            }
        });

        binding.chatButton.setOnClickListener(v -> navigateToChat());
        binding.providerButton.setOnClickListener(v -> navigateToProvider());
        binding.companyButton.setOnClickListener(v -> navigateToCompany());
        return binding.getRoot();
    }

    private void setupFavouriteListeners() {
        serviceViewModel.isFavourite(id).observe(getViewLifecycleOwner(), result -> {
            isFavourite = result;
            favouriteButton.setIconResource(
                    result
                            ? R.drawable.ic_favourite
                            : R.drawable.ic_not_favourite
            );
        });

        favouriteButton.setOnClickListener(v -> {
            if (isFavourite) removeFromFavourites();
            else addToFavourites();
        });
    }

    private void addToFavourites() {
        serviceViewModel.addFavouriteService(id).observe(getViewLifecycleOwner(), name -> {
            if (name != null) {
                isFavourite = true;
                favouriteButton.setIconResource(R.drawable.ic_favourite);
                Toast.makeText(
                        requireContext(),
                        getString(R.string.added_service)
                                + getString(R.string.to_favourites),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void removeFromFavourites() {
        serviceViewModel.removeFavouriteService(id).observe(getViewLifecycleOwner(), result -> {
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