package com.eventorium.presentation.company.fragments;

import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentCompanyRegisterBinding;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.eventorium.presentation.util.ImageUpload;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CompanyRegisterFragment extends Fragment {

    private FragmentCompanyRegisterBinding binding;
    private CompanyViewModel viewModel;
    private CityViewModel cityViewModel;

    private ImageUpload imageUpload;
    private LinearLayout imageContainer;
    private final List<Uri> imageUris = new ArrayList<>();


    public static final String ARG_PROVIDER_ID = "ARG_PROVIDER_ID";
    private Long providerId;

    public CompanyRegisterFragment() {}

    public static CompanyRegisterFragment newInstance(Long providerId) {
        CompanyRegisterFragment fragment = new CompanyRegisterFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROVIDER_ID, providerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            providerId = getArguments().getLong(ARG_PROVIDER_ID);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(CompanyViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);

        imageUpload = new ImageUpload(this, imageUris -> {
            imageContainer.removeAllViews();
            for (Uri uri : imageUris) {
                ImageView imageView = new ImageView(requireContext());
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, 1000);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageURI(uri);
                imageContainer.addView(imageView);
            }
            this.imageUris.addAll(imageUris);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyRegisterBinding.inflate(inflater, container, false);
        ViewCompat.setNestedScrollingEnabled(binding.scroll, true);
        setupNextButton();
        setCities();
        setTimePickers();
        setupImagePicker();
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

    private void setupImagePicker() {
        imageContainer = binding.photosContainer;
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
    }

    private void setupNextButton() {

    }

    private void setTimePickers() {
        TextInputEditText openingTimeEditText = binding.openingTimeEditText;
        TextInputEditText closingTimeEditText = binding.closingTimeEditText;

        openingTimeEditText.setOnClickListener(v -> showTimePicker(openingTimeEditText));
        closingTimeEditText.setOnClickListener(v -> showTimePicker(closingTimeEditText));

    }

    private void showTimePicker(TextInputEditText editText) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minuteOfHour) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minuteOfHour);

                    String time = sdf.format(calendar.getTime());

                    editText.setText(time);
                }, 0, 0, false);

        timePickerDialog.show();
    }

}