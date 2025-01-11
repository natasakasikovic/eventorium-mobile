package com.eventorium.presentation.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.util.constants.ErrorMessages;
import com.eventorium.databinding.FragmentUserProfileBinding;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends Fragment {

    private FragmentUserProfileBinding binding;
    private UserViewModel userViewModel;

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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        loadAccountDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.blockUserButton.setOnClickListener(v -> blockUser());
        binding.reportUserButton.setOnClickListener(v -> reportUser());
    }

    private void loadAccountDetails() {
        if (getArguments() == null) Toast.makeText(requireContext(), ErrorMessages.GENERAL_ERROR, Toast.LENGTH_SHORT).show();
        userViewModel.getUser(getArguments().getLong(ARG_ID)).observe(getViewLifecycleOwner(), result -> {
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

    private void blockUser() {

    }

    private void reportUser() {

    }

}