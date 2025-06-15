package com.eventorium.presentation.user.fragments;

import static com.eventorium.presentation.company.fragments.CompanyRegisterFragment.ARG_ALREADY_VERIFIED;
import static com.eventorium.presentation.company.fragments.CompanyRegisterFragment.ARG_PROVIDER_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.Role;
import com.eventorium.data.auth.models.UpgradeAccountRequest;
import com.eventorium.databinding.FragmentUpgradeAccountBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.auth.viewmodels.RoleViewModel;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpgradeAccountFragment extends Fragment {
    private FragmentUpgradeAccountBinding binding;
    UpgradeAccountRequest request = new UpgradeAccountRequest();
    private RoleViewModel roleViewModel;
    private AuthViewModel authViewModel;
    private Role selectedRole;
    public static String ARG_USER_ID = "ARG_USER_ID";
    private Long userId;

    public UpgradeAccountFragment() { }

    public static UpgradeAccountFragment newInstance(Long userId) {
        UpgradeAccountFragment fragment = new UpgradeAccountFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            userId = getArguments().getLong(ARG_USER_ID);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        roleViewModel = viewModelProvider.get(RoleViewModel.class);
        authViewModel = viewModelProvider.get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpgradeAccountBinding.inflate(inflater, container, false);
        setRoles();
        setupNextButton();
        return binding.getRoot();
    }

    private void setRoles() {
        roleViewModel.getRegistrationRoles().observe(getViewLifecycleOwner(), roles -> {
            ArrayAdapter<Role> roleArrayAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    roles
            );
            binding.spinnerRole.setAdapter(roleArrayAdapter);
        });
    }

    private void getFormFields() {
        request.setAddress(binding.inputAddress.getText().toString());
        request.setPhoneNumber(binding.inputPhone.getText().toString());
        selectedRole = (Role) binding.spinnerRole.getSelectedItem();
        request.setRole(selectedRole);
    }

    private void setupNextButton() {
        binding.buttonNext.setOnClickListener(v -> {
            getFormFields();
            sendUpgradeRequest();
            nextStep();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void sendUpgradeRequest() {
        authViewModel.upgradeAccount(request).observe(getViewLifecycleOwner(), res -> {
            if (res.getData() != null) {
                authViewModel.updateSession(res.getData());
            } else {
                Toast.makeText(requireContext(), res.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextStep() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        if (selectedRole.getName().equals("PROVIDER")) {
            Bundle args = new Bundle();
            args.putLong(ARG_PROVIDER_ID, userId);
            args.putBoolean(String.valueOf(ARG_ALREADY_VERIFIED), true);
            navController.navigate(R.id.upgrade_account_to_company_register, args);
        } else {
            ((MainActivity) requireActivity()).refresh(selectedRole.getName());
            navController.popBackStack(R.id.homepageFragment, false);
        }
    }
}