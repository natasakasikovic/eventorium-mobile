package com.eventorium.presentation.user.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.models.Person;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentAccountEditBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.shared.dialogs.ConfirmationDialog;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.eventorium.presentation.user.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountEditFragment extends Fragment {

    private FragmentAccountEditBinding binding;
    private ArrayAdapter<City> cityArrayAdapter;
    private UserViewModel viewModel;
    private CityViewModel cityViewModel;
    private Person updateRequest;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public AccountEditFragment () { }

    public static AccountEditFragment newInstance() {
        return new AccountEditFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(UserViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        binding.profileImage.setImageURI(selectedImageUri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountEditBinding.inflate(inflater, container, false);
        setCities();
        setupSaveButton();
        setupImagePicker();
        setupDeactivateButton();
        loadAccountDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.changePasswordButton.setOnClickListener(v -> {
            showChangePasswordDialog();
        });
    }

    private void setupImagePicker() {
        binding.profilePhotoEdit.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
    }

    private void setCities() {
        cityViewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            cityArrayAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    cities
            );
            binding.spinnerCity.setAdapter(cityArrayAdapter);
        });
    }

    private void showChangePasswordDialog() {
        PasswordEditFragment dialogFragment = PasswordEditFragment.newInstance();
        dialogFragment.show(getParentFragmentManager(), "PasswordEditDialog");
    }

    private void setupDeactivateButton() {
        Button btn = binding.deactivateAccountButton;
        btn.setOnClickListener(v -> showConfirmationDialog());
    }

    public void showConfirmationDialog() {
        new ConfirmationDialog(requireContext())
                .setMessage(getString(R.string.deactivate_confirmation))
                .setOnConfirmButtonListener(this::deactivate)
                .show();
    }

    private void deactivate() {
        viewModel.deactivateAccount().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                showMessage(getString(R.string.account_deactivated));
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.logOutUser();
            } else {
                showMessage(result.getError());
            }
        });
    }

    private void showMessage(String message) {
        new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void loadAccountDetails() {
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                AccountDetails accountDetails = result.getData();
                binding.nameEditText.setText(accountDetails.getName());
                binding.lastNameEditText.setText(accountDetails.getLastname());
                binding.addressEditText.setText(accountDetails.getAddress());
                City accountCity = accountDetails.getCity();
                if (cityArrayAdapter != null && accountCity != null) {
                    int position = cityArrayAdapter.getPosition(accountCity);
                    binding.spinnerCity.setSelection(position);
                }
                binding.phoneEditText.setText(accountDetails.getPhoneNumber());
                binding.emailText.setText(accountDetails.getEmail());
                loadProfilePhoto(accountDetails.getId());
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfilePhoto(Long userId) {
        binding.profileImageLoader.setVisibility(View.VISIBLE);
        viewModel.getProfilePhoto(userId).observe(getViewLifecycleOwner(), profilePhoto -> {
            if (profilePhoto != null)
                binding.profileImage.setImageBitmap(profilePhoto);
            else
                binding.profileImage.setImageResource(R.drawable.profile_photo);
            binding.profileImageLoader.setVisibility(View.GONE);
        });
    }

    private void setupSaveButton() {
        binding.saveButton.setOnClickListener(v -> {
            if (areFieldsEmpty()) {
                Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            getFormFields();
            viewModel.update(updateRequest).observe(getViewLifecycleOwner(), result -> {
                 if (result.getError() == null) {
                     if (selectedImageUri != null) uploadProfilePhoto();
                     else navigateToAccountDetails();
                 }
                 else {
                     Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_LONG).show();
                 }
             });
        });
    }

    private boolean areFieldsEmpty() {
        return TextUtils.isEmpty(binding.nameEditText.getText()) &&
                TextUtils.isEmpty(binding.lastNameEditText.getText()) &&
                TextUtils.isEmpty(binding.addressEditText.getText()) &&
                TextUtils.isEmpty(binding.phoneEditText.getText());
    }

    private void getFormFields() {
        updateRequest = new Person();
        updateRequest.setName(binding.nameEditText.getText().toString());
        updateRequest.setLastname(binding.lastNameEditText.getText().toString());
        updateRequest.setAddress(binding.addressEditText.getText().toString());
        updateRequest.setPhoneNumber(binding.phoneEditText.getText().toString());
        updateRequest.setCity((City) binding.spinnerCity.getSelectedItem());
    }

    private void uploadProfilePhoto() {
        viewModel.updateProfilePhoto(getContext(), selectedImageUri)
                .observe(getViewLifecycleOwner(), success -> {
                    if (!success) Toast.makeText(requireContext(),
                            R.string.profile_photo_not_added,
                            Toast.LENGTH_LONG).show();
                    navigateToAccountDetails();
                });
    }

    private void navigateToAccountDetails() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigateUp();
    }

}