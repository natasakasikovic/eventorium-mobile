package com.eventorium.presentation.auth.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.eventorium.R;
import com.eventorium.databinding.FragmentRegisterBinding;
import com.eventorium.presentation.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AutoCompleteTextView roleDropdown;
    private List<String> roleNames;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        setupCityAutoCompleteAdapter();
        setupRoleDropdown();
        showRoleInfoDialog();
        setupNextButton();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupCityAutoCompleteAdapter() {
        // TODO: Replace the hardcoded list of cities with a dynamic list
        List<String> cities = Arrays.asList("New York", "Los Angeles", "London", "Paris", "Tokyo", "Berlin", "Novi Sad");

        AutoCompleteTextView cityAutoComplete = binding.cityAutoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, cities);
        cityAutoComplete.setAdapter(adapter);

        cityAutoComplete.setThreshold(0);
    }

    private void setupRoleDropdown() {
        roleDropdown = binding.roleDropdown;

        roleNames = Arrays.asList(
                getString(R.string.service_and_product_provider),
                getString(R.string.event_organizer)
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, roleNames);
        roleDropdown.setAdapter(adapter);

        roleDropdown.setOnClickListener(v -> {
            roleDropdown.requestFocus();
            roleDropdown.showDropDown();
        });

        roleDropdown.setKeyListener(null);
        roleDropdown.setFocusable(false);
    }

    private void showRoleInfoDialog() {

        TextInputLayout roleDropdownLayout = binding.roleDropdownLayout;

        roleDropdownLayout.setEndIconOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.role_info_message));
            builder.setPositiveButton(getString(R.string.ok_button), (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    private void setupNextButton() {
        ImageButton button = binding.arrowButton;

        button.setOnClickListener(v -> {
            String selectedRole = roleDropdown.getText().toString();
            if (selectedRole.equals(getString(R.string.service_and_product_provider))) {
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.user_register_to_company_register);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.activation_dialog_message));
                builder.setPositiveButton(getString(R.string.ok_button), (dialog, which) -> {
                    dialog.dismiss();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    getActivity().finish();
                });
                builder.create().show();
            }
        });
    }


}