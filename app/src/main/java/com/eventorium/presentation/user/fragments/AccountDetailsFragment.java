package com.eventorium.presentation.user.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.databinding.FragmentAccountDetailsBinding;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountDetailsFragment extends Fragment {

    private FragmentAccountDetailsBinding binding;
    private UserViewModel userViewModel;

    public AccountDetailsFragment() { }

    public static AccountDetailsFragment newInstance() {
        return new AccountDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        loadAccountDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.editAccountButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_accountDetails_to_editAccountFragment);
        });
    }

    private void loadAccountDetails() {
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), result -> {
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
                Toast.makeText(requireContext(), "Error while loading account.", Toast.LENGTH_SHORT).show();
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

}