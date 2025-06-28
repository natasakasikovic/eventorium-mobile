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
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.UserDetails;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.models.budget.BudgetItemRequest;
import com.eventorium.data.interaction.models.review.ReviewType;
import com.eventorium.data.solution.models.SolutionType;
import com.eventorium.data.solution.models.service.Service;
import com.eventorium.databinding.FragmentServiceDetailsBinding;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.eventorium.presentation.event.viewmodels.BudgetViewModel;
import com.eventorium.presentation.interaction.fragments.chat.ChatFragment;
import com.eventorium.presentation.company.fragments.CompanyDetailsFragment;
import com.eventorium.presentation.interaction.fragments.comment.CommentsOverviewFragment;
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
    private BudgetViewModel budgetViewModel;
    private MaterialButton favouriteButton;
    private boolean isFavourite;
    public static final String ARG_ID = "ARG_SERVICE_ID";
    public static final String ARG_PLANNED_AMOUNT = "ARG_PLANNED_AMOUNT";
    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private Long id;
    private Double plannedAmount;
    private Long eventId;
    private UserDetails provider;
    private Category category;
    private Long companyId;
    private Integer maxDuration;
    private Integer minDuration;


    public ServiceDetailsFragment() { }

    public static ServiceDetailsFragment newInstance(Long id) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static ServiceDetailsFragment newInstance(Long id, Long eventId, Double plannedAmount) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putDouble(ARG_PLANNED_AMOUNT, plannedAmount);
        args.putLong(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
            plannedAmount = getArguments().getDouble(ARG_PLANNED_AMOUNT);
            eventId = getArguments().getLong(ARG_EVENT_ID);
        }

        ViewModelProvider provider = new ViewModelProvider(this);
        serviceViewModel = provider.get(ServiceViewModel.class);
        loginViewModel = provider.get(LoginViewModel.class);
        budgetViewModel = provider.get(BudgetViewModel.class);
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
                minDuration = service.getMinDuration();
                maxDuration = service.getMaxDuration();
                category = service.getCategory();
                renderButtons();
            }
        });

        setupClickListeners();

        return binding.getRoot();
    }

    private void setupClickListeners() {
        binding.chatButton.setOnClickListener(v -> navigateToChat());
        binding.providerButton.setOnClickListener(v -> navigateToProvider());
        binding.companyButton.setOnClickListener(v -> navigateToCompany());
        binding.addToPlannedButton.setOnClickListener(v -> addToPlanner());
        binding.seeCommentsButton.setOnClickListener(v -> navigateToComments());
        binding.reserveService.setOnClickListener(v -> navigateToReservation());
        binding.backToPlannerButton.setOnClickListener(v -> navigateToBudget());
    }

    private void addToPlanner() {
        BudgetItemRequest item = BudgetItemRequest.builder()
                .itemId(id)
                .plannedAmount(plannedAmount)
                .itemType(SolutionType.SERVICE)
                .category(category)
                .build();

        budgetViewModel.createBudgetItem(eventId, item).observe(getViewLifecycleOwner(), result -> {
            if(result.getError() == null) {
                Toast.makeText(
                        requireContext(),
                        R.string.successfully_added_service_to_planner,
                        Toast.LENGTH_SHORT
                ).show();
                navigateToBudget();
            } else {
                Toast.makeText(
                        requireContext(),
                        result.getError(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void navigateToBudget() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.popBackStack();
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

    private void navigateToComments() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putSerializable(CommentsOverviewFragment.ARG_TYPE, ReviewType.SERVICE);
        args.putLong(CommentsOverviewFragment.ARG_ID, id);
        navController.navigate(R.id.action_serviceDetails_to_comments, args);
    }

    private void navigateToReservation() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);

        Integer fixedDurationHours = minDuration.equals(maxDuration) ? minDuration : 0;

        if (eventId == 0)
            navController.navigate(R.id.action_serviceDetails_to_reservation, ReserveServiceFragment.newInstance(id, fixedDurationHours).getArguments());
        else
            navController.navigate(R.id.action_serviceDetails_to_reservation, ReserveServiceFragment.newInstance(id, fixedDurationHours, eventId, plannedAmount).getArguments());
    }

    private void navigateToProvider() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(UserProfileFragment.ARG_ID, provider.getId());
        navController.navigate(R.id.action_serviceDetails_to_provider, args);
    }

    private void renderButtons() {
        String role = loginViewModel.getUserRole();
        Long userId = loginViewModel.getUserId();
        if(role == null || role.isEmpty()) {
            binding.favButton.setVisibility(View.GONE);
            binding.chatButton.setVisibility(View.GONE);
            changeMargin();
            return;
        }
        if(!role.equals("EVENT_ORGANIZER")) {
            binding.chatButton.setVisibility(View.GONE);
        } else {
            binding.reserveService.setVisibility(View.VISIBLE);
        }

        if(role.equals("PROVIDER") && userId.equals(provider.getId())) {
            binding.providerButton.setVisibility(View.GONE);
            binding.providerName.setVisibility(View.GONE);
            binding.companyButton.setVisibility(View.GONE);
            binding.companyName.setVisibility(View.GONE);
        }

        if(eventId != 0) {
            binding.backToPlannerButton.setVisibility(View.VISIBLE);
            binding.addToPlannedButton.setVisibility(View.VISIBLE);
        }
    }

    private void changeMargin() {
        TextView serviceTextView = binding.serviceName;
        float density = getResources().getDisplayMetrics().density;
        int leftMargin = (int) (20 * density);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) serviceTextView.getLayoutParams();
        layoutParams.setMargins(leftMargin, layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

        serviceTextView.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}