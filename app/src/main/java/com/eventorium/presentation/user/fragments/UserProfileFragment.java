package com.eventorium.presentation.user.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.databinding.FragmentUserProfileBinding;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.shared.dialogs.ConfirmationDialog;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;
    private Long id;

    public static final String ARG_ID = "ARG_USER_ID";

    public UserProfileFragment() {}

    public static UserProfileFragment newInstance(Long id) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        ViewModelProvider provider = new ViewModelProvider(this);
        userViewModel = provider.get(UserViewModel.class);
        authViewModel = provider.get(AuthViewModel.class);
        loadAccountDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (authViewModel.getUserId() == null || authViewModel.getUserId() == id) hideButtons();

        binding.blockUserButton.setOnClickListener(v -> openBlockConfirmationDialog());
        binding.reportUserButton.setOnClickListener(v -> navigateToReportUser());
    }

    private void loadAccountDetails() {
        if (getArguments() == null) Toast.makeText(requireContext(), ErrorMessages.GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        id = getArguments().getLong(ARG_ID);
        userViewModel.getUser(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                AccountDetails accountDetails = result.getData();
                String fullName = accountDetails.getName() + " " + accountDetails.getLastname();
                String address = accountDetails.getAddress() + ", " + accountDetails.getCity().getName();
                binding.fullName.setText(fullName);
                binding.addressCity.setText(address);
                binding.phoneNumber.setText(accountDetails.getPhoneNumber());
                binding.email.setText(accountDetails.getEmail());
                loadProfilePhoto(accountDetails.getId());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfilePhoto(Long userId) {
        binding.profileImageLoader.setVisibility(View.VISIBLE);
        userViewModel.getProfilePhoto(userId).observe(getViewLifecycleOwner(), profilePhoto -> {
            if (profilePhoto != null)
                binding.profileImage.setImageBitmap(profilePhoto);
            else
                binding.profileImage.setImageResource(R.drawable.profile_photo);
            binding.profileImageLoader.setVisibility(View.GONE);
        });
    }

    private void hideButtons() {
        binding.blockUserButton.setVisibility(View.GONE);
        binding.reportUserButton.setVisibility(View.GONE);
    }

    private void openBlockConfirmationDialog() {
        new ConfirmationDialog(requireContext())
                .setMessage("Are you sure you want to block this user? This action is permanent and cannot be undone!")
                .setOnConfirmButtonListener(this::blockUser)
                .show();
    }

    private void blockUser() {
        userViewModel.blockUser(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                Toast.makeText(requireContext(), "User is successfully blocked!", Toast.LENGTH_SHORT).show();
                navigateToHomepage();
            }
            else
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToHomepage() {
        NavController navController = Navigation.findNavController( requireActivity(), R.id.fragment_nav_content_main );
        navController.popBackStack(R.id.homepageFragment, false);
    }

    private void navigateToReportUser() {
        NavController navController = Navigation.findNavController( requireActivity(), R.id.fragment_nav_content_main );
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        navController.navigate(R.id.action_profile_to_report, args);
    }

}