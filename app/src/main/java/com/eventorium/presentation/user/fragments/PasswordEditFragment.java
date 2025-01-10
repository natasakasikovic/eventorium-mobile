package com.eventorium.presentation.user.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.ChangePasswordRequest;
import com.eventorium.databinding.FragmentPasswordEditBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PasswordEditFragment extends DialogFragment {

    private FragmentPasswordEditBinding binding;
    private ChangePasswordRequest request;

    private UserViewModel userViewModel;

    public PasswordEditFragment() { }

    public static PasswordEditFragment newInstance() { return new PasswordEditFragment(); }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        userViewModel = provider.get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPasswordEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSubmit.setOnClickListener(v -> saveNewPassword());
    }

    private void saveNewPassword() {
        if (areFieldsEmpty()) {
            Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        getFormFields();
        userViewModel.changePassword(request).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                showInfoDialog();
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.logOutUser();
                dismiss();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.password_changed_title)
                .setMessage(R.string.logout_reason)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private boolean areFieldsEmpty() {
        return TextUtils.isEmpty(binding.editTextOldPassword.getText()) &&
                TextUtils.isEmpty(binding.editTextNewPassword.getText()) &&
                TextUtils.isEmpty(binding.editTextConfirmPassword.getText());
    }

    private void getFormFields() {
        request = new ChangePasswordRequest();
        request.setOldPassword(binding.editTextOldPassword.getText().toString());
        request.setPassword(binding.editTextNewPassword.getText().toString());
        request.setPasswordConfirmation(binding.editTextConfirmPassword.getText().toString());
    }
}