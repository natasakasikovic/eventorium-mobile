package com.eventorium.presentation.company.fragments;

import static java.util.stream.Collectors.toList;

import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.eventorium.data.company.models.Company;
import com.eventorium.data.shared.models.City;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.databinding.FragmentEditCompanyBinding;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.models.RemoveImageRequest;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.eventorium.presentation.shared.models.ImageItem;
import com.eventorium.data.shared.utils.ImageUpload;
import com.eventorium.presentation.shared.adapters.ImageAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditCompanyFragment extends Fragment {

    private FragmentEditCompanyBinding binding;
    private NavController navController;
    private Long id;
    private CompanyViewModel viewModel;
    private CityViewModel cityViewModel;
    private ArrayAdapter<City> cityArrayAdapter;

    private ImageUpload imageUpload;
    private final List<Uri> imageUris = new ArrayList<>();
    private ImageAdapter newImagesAdapter;
    private ImageAdapter existingImagesAdapter;
    private List<RemoveImageRequest> removedImages = new ArrayList<>();

    public EditCompanyFragment() { }

    public static EditCompanyFragment newInstance() {
        return new EditCompanyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(CompanyViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCompanyBinding.inflate(inflater, container, false);
        setupImagePicker();
        existingImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
           removedImages.add(new RemoveImageRequest(imageItem.getId()));
           if (existingImagesAdapter.getItemCount() == 1) this.binding.images.setVisibility(View.GONE);
        });
        binding.images.setAdapter(existingImagesAdapter);
        binding.saveButton.setOnClickListener(v -> save());
        binding.newImagesCard.setVisibility(View.GONE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImageUpload();
        setCities();
        setupTimePickers();
        loadCompany();
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

    private void setupImageUpload() {
        imageUpload = new ImageUpload(this, imageUris -> {
            newImagesAdapter.insert(imageUris.stream()
                    .map(uri -> {
                        try {
                            Bitmap bitmap = FileUtil.convertToBitmap(requireContext(), uri);
                            return new ImageItem(bitmap, uri);
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(toList()));
            this.imageUris.addAll(imageUris);
            this.binding.newImagesCard.setVisibility(View.VISIBLE);
        });
    }

    private void setupImagePicker() {
        newImagesAdapter = new ImageAdapter(new ArrayList<>(), imageItem -> {
            imageUris.remove(imageItem.getUri());
            if (imageUris.isEmpty()) this.binding.newImagesCard.setVisibility(View.GONE);
        });
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
        binding.newImages.setAdapter(newImagesAdapter);
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

    void loadCompany() {
        viewModel.getCompany().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                Company company = result.getData();
                id = company.getId();
                binding.name.setText(company.getName());
                binding.email.setText(company.getEmail());
                binding.description.setText(company.getDescription());
                binding.address.setText(company.getAddress());
                City city = company.getCity();
                if (cityArrayAdapter != null && city != null) {
                    int position = cityArrayAdapter.getPosition(city);
                    binding.spinnerCity.setSelection(position);
                }
                binding.phoneNumber.setText(company.getPhoneNumber());
                binding.openingTimeEditText.setText(company.getOpeningHours().toLowerCase());
                binding.closingTimeEditText.setText(company.getClosingHours().toLowerCase());
                loadImages();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImages() {
        this.viewModel.getImages(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                List<ImageItem> images = result.getData();
                if (images.isEmpty()) this.binding.images.setVisibility(View.GONE);
                else existingImagesAdapter.insert(images);
                binding.loader.setVisibility(View.GONE);
            } else {
                Toast.makeText(requireContext(), "Error while loading images", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void save() {
        Company company = Company.builder()
            .id(id)
            .name(binding.name.getText().toString())
            .address(binding.address.getText().toString())
            .city((City) binding.spinnerCity.getSelectedItem())
            .description(binding.description.getText().toString())
            .phoneNumber(binding.phoneNumber.getText().toString())
            .openingHours(binding.openingTimeEditText.getText().toString().toUpperCase())
            .closingHours(binding.closingTimeEditText.getText().toString().toUpperCase())
            .build();
        viewModel.update(company).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                if (removedImages.isEmpty() && imageUris.isEmpty()) navController.navigateUp();
                else {
                    removeImages();
                    uploadNewImages();
                }
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void removeImages() {
        if (!removedImages.isEmpty()) {
            viewModel.removeImages(removedImages).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() != null)
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            });
        } else {
            if (imageUris.isEmpty()) navController.navigateUp();
        }
    }

    void uploadNewImages() {
        if (!imageUris.isEmpty())
            viewModel.uploadImages(id, requireContext(), imageUris).observe(getViewLifecycleOwner(), success -> {
                if (!success)
                    Toast.makeText(requireContext(), "Error while uploading images", Toast.LENGTH_SHORT).show();
                navController.navigateUp();
            });
        else navController.navigateUp();
    }
}