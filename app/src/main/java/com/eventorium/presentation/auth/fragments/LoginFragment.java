package com.eventorium.presentation.auth.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.dtos.LoginRequestDto;
import com.eventorium.data.auth.dtos.LoginResponseDto;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.services.WebSocketService;
import com.eventorium.databinding.FragmentLoginBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.auth.viewmodels.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private ActivityResultLauncher<String> requestNotificationPermissionLauncher;
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;

    private LoginResponseDto response;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        subscribeToNotifications();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        emailEditText = binding.emailEditText;
        passwordEditText = binding.passwordEditText;
        binding.signInButton.setOnClickListener(v -> login());

        return binding.getRoot();
    }

    private void login() {

        String email = Objects.requireNonNull(emailEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        LoginRequestDto dto = new LoginRequestDto(email, password);
        loginViewModel.login(dto).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                response = result.getData();
                requestNotificationPermission();
            } else {
                Toast.makeText(
                        requireContext(),
                        result.getError(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    private void navigateToHome() {
        NavController navController = NavHostFragment.findNavController(this);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        navController.navigate(R.id.homepageFragment, null, navOptions);
        String role = loginViewModel.saveRole(response.getJwt());
        ((MainActivity) getActivity()).refresh(role);
    }

    private void subscribeToNotifications() {
        requestNotificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        loginViewModel.openWebSocket();
                    }
                    navigateToHome();
                }
        );
    }

    public void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            navigateToHome();
            loginViewModel.openWebSocket();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}