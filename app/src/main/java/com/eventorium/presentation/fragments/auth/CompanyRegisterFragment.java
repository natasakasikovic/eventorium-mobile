package com.eventorium.presentation.fragments.auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.eventorium.R;
import com.eventorium.databinding.FragmentCompanyRegisterBinding;

public class CompanyRegisterFragment extends Fragment {

    private FragmentCompanyRegisterBinding binding;

    public CompanyRegisterFragment() {}

    public static CompanyRegisterFragment newInstance() {
        return new CompanyRegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyRegisterBinding.inflate(inflater, container, false);
        ViewCompat.setNestedScrollingEnabled(binding.scroll, true);
        setupNextButton();
        return binding.getRoot();
    }

    private void setupNextButton() {
        ImageButton button = binding.arrowButton;
        button.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.activation_dialog_title))
                    .setMessage(getString(R.string.activation_dialog_message))
                    .setPositiveButton(getString(R.string.ok_button), (dialog, which) -> {
                        Intent intent = requireActivity().getIntent();
                        requireActivity().finish();
                        startActivity(intent);
                    })
                    .setCancelable(false)
                    .show();
        });
    }
}