package com.eventorium.presentation.company.fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.company.models.Company;
import com.eventorium.data.company.models.CreateCompany;
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
    private CreateCompany company;

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

        setupImagesUpload();
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

    private void setupImagePicker() {
        imageContainer = binding.photosContainer;
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
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
                            .observe(getViewLifecycleOwner(), this::handleUpload);
                }
                showInfoDialog();
                NavController navController = Navigation.findNavController(requireActivity(),
                                                            R.id.fragment_nav_content_main);
                navController.popBackStack(R.id.homepageFragment, false);
            } else {
                Toast.makeText(requireContext(), response.getError(), Toast.LENGTH_LONG).show();
            }
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

    private void handleUpload(boolean successful) {
        if (!successful) {
            Toast.makeText(requireContext(),
                    "Images upload failed. Please update images later.",
                    Toast.LENGTH_LONG).show();
        }
    }


    private void setupImagesUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            imageContainer.removeAllViews();
            this.imageUris.clear();
            long totalSize = 0;
            int maxSize = getResources().getInteger(R.integer.max_request_size);
            long maxRequestSizeInBytes = maxSize * 1024L * 1024L;

            for (Uri uri : imageUris) {
                if (shouldAddImage(uri, maxRequestSizeInBytes, totalSize)) {
                    totalSize += getImageSize(uri);
                    addImageToView(uri);
                }
            }
        });
    }

    private boolean shouldAddImage(Uri uri, long maxRequestSizeInBytes, long totalSize) {
        long fileSize = getImageSize(uri);
        if (fileSize + totalSize > maxRequestSizeInBytes) {
            Toast.makeText(requireContext(),
                    "Some photos or the total size exceed the allowed limit.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private long getImageSize(Uri uri) {
        try (Cursor cursor = requireContext().getContentResolver()
                .query(uri, null, null, null, null)) {
            if (cursor != null) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                cursor.moveToFirst();
                return cursor.getLong(sizeIndex);
            }
        } catch (Exception e) {
            Log.e("IMAGE ERROR", "Error while getting image size", e);
        }
        return 0;
    }

    private void addImageToView(Uri uri) {
        ImageView imageView = new ImageView(requireContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, 1000);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageURI(uri);
        imageContainer.addView(imageView);
        this.imageUris.add(uri);
    }

}