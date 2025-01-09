package com.eventorium.presentation.auth.fragments;

import static com.eventorium.presentation.company.fragments.CompanyRegisterFragment.ARG_PROVIDER_ID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.auth.models.Person;
import com.eventorium.data.auth.models.Role;
import com.eventorium.data.auth.models.User;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentRegisterBinding;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.auth.viewmodels.RoleViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel viewModel;
    private CityViewModel cityViewModel;
    private RoleViewModel roleViewModel;

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private Uri selectedImageUri;

    private User user;
    private Role selectedRole;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = viewModelProvider.get(AuthViewModel.class);
        cityViewModel = viewModelProvider.get(CityViewModel.class);
        roleViewModel = viewModelProvider.get(RoleViewModel.class);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        binding.profileImageView.setImageURI(selectedImageUri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        setCities();
        setRoles();
        setupNextButton();
        setupImagePicker();
        return binding.getRoot();
    }

    private void setupImagePicker() {
        binding.uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
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

    private void setCities() {
        cityViewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    cities
            );
            binding.spinnerCity.setAdapter(cityArrayAdapter);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupNextButton() {
        ImageButton button = binding.arrowButton;
        button.setOnClickListener(v -> signup());
    }

    private void signup() {
        if (areFieldsEmpty()) {
            Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        getFormFields();
        viewModel.createAccount(user).observe(getViewLifecycleOwner(), response -> {
            if (response.getData() != null) {
                uploadProfilePhoto(response.getData());
                nextStep(response.getData());
            } else  {
                Toast.makeText(requireContext(), response.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void nextStep(User user) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        if (selectedRole.getName().equals("PROVIDER")) {
            Bundle args = new Bundle();
            args.putLong(ARG_PROVIDER_ID, user.getId());
            navController.navigate(R.id.companyRegisterFragment, args);
        } else {
            showInfoDialog();
            navController.popBackStack(R.id.homepageFragment, false);
        }
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.activation_dialog_title)
                .setMessage(R.string.activation_dialog_message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void uploadProfilePhoto(User user) {
        if (selectedImageUri != null)
            viewModel.uploadProfilePhoto(user.getId(), getContext(), selectedImageUri)
                    .observe(getViewLifecycleOwner(), success -> {
                        if (!success) Toast.makeText(requireContext(),
                                R.string.profile_photo_not_added,
                                Toast.LENGTH_LONG).show();
                    });
    }

    private void getFormFields() {
        user = new User();
        user.setEmail(binding.emailEditText.getText().toString());
        user.setPassword(binding.passwordEditText.getText().toString());
        user.setConfirmPassword(binding.confirmPasswordEditText.getText().toString());
        selectedRole = (Role) binding.spinnerRole.getSelectedItem();
        user.setRoles(Collections.singletonList(selectedRole));

        Person person = new Person();
        person.setName(binding.nameEditText.getText().toString());
        person.setLastname(binding.lastNameEditText.getText().toString());
        person.setAddress(binding.addressEditText.getText().toString());
        person.setPhoneNumber(binding.numberEditText.getText().toString());
        person.setCity((City) binding.spinnerCity.getSelectedItem());

        user.setPerson(person);
    }

    private boolean areFieldsEmpty() {
        return TextUtils.isEmpty(binding.emailEditText.getText()) &&
                TextUtils.isEmpty(binding.passwordEditText.getText()) &&
                TextUtils.isEmpty(binding.confirmPasswordEditText.getText()) &&
                TextUtils.isEmpty(binding.nameEditText.getText()) &&
                TextUtils.isEmpty(binding.lastNameEditText.getText()) &&
                TextUtils.isEmpty(binding.addressEditText.getText()) &&
                TextUtils.isEmpty(binding.addressEditText.getText()) &&
                TextUtils.isEmpty(binding.numberEditText.getText());
    }

}