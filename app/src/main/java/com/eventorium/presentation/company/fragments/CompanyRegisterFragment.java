package com.eventorium.presentation.company.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.company.models.CreateCompany;
import com.eventorium.data.shared.models.City;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.utils.ImageUpload;
import com.eventorium.databinding.FragmentCompanyRegisterBinding;
import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CompanyRegisterFragment extends Fragment {

    private FragmentCompanyRegisterBinding binding;
    private CompanyViewModel viewModel;
    private CityViewModel cityViewModel;

    private CreateCompany company;
    private ImageAdapter imageAdapter;
    private ImageUpload imageUpload;
    private final List<Uri> imageUris = new ArrayList<>();


    public static final String ARG_PROVIDER_ID = "ARG_PROVIDER_ID";
    public static final Boolean ARG_ALREADY_VERIFIED = false;

    private Long providerId;
    private Boolean isUserAlreadyVerified;

    public CompanyRegisterFragment() {}

    public static CompanyRegisterFragment newInstance(Long providerId, Boolean isUserAlreadyVerified) {
        CompanyRegisterFragment fragment = new CompanyRegisterFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROVIDER_ID, providerId);
        args.putBoolean(String.valueOf(ARG_ALREADY_VERIFIED), isUserAlreadyVerified);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            providerId = getArguments().getLong(ARG_PROVIDER_ID);
            isUserAlreadyVerified = getArguments().getBoolean(String.valueOf(ARG_ALREADY_VERIFIED));
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(CompanyViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);
        setupImageUpload();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyRegisterBinding.inflate(inflater, container, false);
        ViewCompat.setNestedScrollingEnabled(binding.scroll, true);
        setCities();
        setupTimePickers();
        setupImagePicker();
        binding.arrowButton.setOnClickListener(v -> registerCompany());
        return binding.getRoot();
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

    private void setupImageUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            imageAdapter.insert(imageUris.stream()
                    .map(uri -> {
                        try {
                            Bitmap bitmap = FileUtil.convertToBitmap(requireContext(), uri);
                            return new ImageItem(bitmap, uri);
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            this.imageUris.addAll(imageUris);
        });
    }

    private void setupTimePickers() {
        TextInputEditText openingTimeEditText = binding.openingTimeEditText;
        TextInputEditText closingTimeEditText = binding.closingTimeEditText;

        openingTimeEditText.setOnClickListener(v -> showTimePicker(openingTimeEditText));
        closingTimeEditText.setOnClickListener(v -> showTimePicker(closingTimeEditText));

    }

    private void showTimePicker(TextInputEditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minuteOfHour);

                    String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.getTime());

                    editText.setText(time);
                }, 0, 0, false
        );

        timePickerDialog.show();
    }


    private void registerCompany() {
        if (!areFieldsValid()) {
            Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        getFormFields();

        viewModel.registerCompany(company).observe(getViewLifecycleOwner(), response -> {
            if (response.getData() != null) {
                if (!imageUris.isEmpty()) {
                    viewModel.uploadImages(response.getData().getId(), getContext(), imageUris)
                            .observe(getViewLifecycleOwner(), success -> {
                                if (!success) Toast.makeText(requireContext(), "Error while uploading images", Toast.LENGTH_SHORT).show();
                            });
                }

                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                if (!isUserAlreadyVerified)
                    showInfoDialog();
                else
                    ((MainActivity) requireActivity()).refresh("PROVIDER");

                navController.popBackStack(R.id.homepageFragment, false);
            }
            else Toast.makeText(requireContext(), response.getError(), Toast.LENGTH_LONG).show();
        });

    }

    private void getFormFields() {
        company = new CreateCompany();
        company.setEmail(binding.emailEditText.getText().toString());
        company.setName(binding.nameEditText.getText().toString());
        company.setAddress(binding.addressEditText.getText().toString());
        company.setDescription(binding.descriptionEditText.getText().toString());
        company.setPhoneNumber(binding.numberEditText.getText().toString());
        company.setOpeningHours(binding.openingTimeEditText.getText().toString().toUpperCase());
        company.setClosingHours(binding.closingTimeEditText.getText().toString().toUpperCase());
        company.setCity((City) binding.spinnerCity.getSelectedItem());
        company.setProviderId(providerId);
    }

    private boolean areFieldsValid() {
        return !TextUtils.isEmpty(binding.emailEditText.getText()) &&
                !TextUtils.isEmpty(binding.nameEditText.getText()) &&
                !TextUtils.isEmpty(binding.addressEditText.getText()) &&
                !TextUtils.isEmpty(binding.numberEditText.getText()) &&
                !TextUtils.isEmpty(binding.descriptionEditText.getText()) &&
                !TextUtils.isEmpty(binding.openingTimeEditText.getText()) &&
                !TextUtils.isEmpty(binding.closingTimeEditText.getText());
    }

    private void showInfoDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.activation_dialog_title)
                .setMessage(R.string.activation_dialog_message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setupImagePicker() {
        imageAdapter = new ImageAdapter(new ArrayList<>(),
                imageItem -> imageUris.remove(imageItem.getUri()));
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
        binding.newImages.setAdapter(imageAdapter);
    }


}