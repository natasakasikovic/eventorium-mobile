package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.category.models.Category;
import com.eventorium.data.event.mappers.EventTypeMapper;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.solution.dtos.CreateServiceRequestDto;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.util.models.ReservationType;
import com.eventorium.databinding.FragmentCreateServiceBinding;
import com.eventorium.presentation.category.viewmodels.CategoryViewModel;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.util.ImageUpload;
import com.eventorium.presentation.util.adapters.ChecklistAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateServiceFragment extends Fragment {
    private FragmentCreateServiceBinding binding;
    private final List<Uri> imageUris = new ArrayList<>();
    private ImageUpload imageUpload;
    private LinearLayout imageContainer;
    private ServiceViewModel serviceViewModel;
    private EventTypeViewModel eventTypeViewModel;
    private CategoryViewModel categoryViewModel;

    public CreateServiceFragment() {
    }

    public static CreateServiceFragment newInstance() {
        return new CreateServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        serviceViewModel = provider.get(ServiceViewModel.class);
        categoryViewModel = provider.get(CategoryViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        imageUpload = new ImageUpload(this, imageUris -> {
            imageContainer.removeAllViews();
            for (Uri uri : imageUris) {
                ImageView imageView = new ImageView(requireContext());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setPadding(50, 0, 50, 0);
                imageView.setImageURI(uri);
                imageContainer.addView(imageView);
            }
            this.imageUris.addAll(imageUris);
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateServiceBinding.inflate(inflater, container, false);
        createDatePickers();
        loadCategories();
        loadEventTypes();
        binding.manualChecked.setChecked(true);
        setupImagePicker();
        binding.createServiceButton.setOnClickListener(v -> createService());
        return binding.getRoot();
    }

    private TextInputEditText reservationDate;
    private TextInputEditText cancellationDate;
    private void createDatePickers() {
        reservationDate = binding.serviceReservationDeadlineText;
        cancellationDate = binding.serviceCancellationDeadlineText;

        MaterialDatePicker<Long> reservationPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .build();
        MaterialDatePicker<Long> cancellationPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .build();


        reservationDate.setOnClickListener(v ->
                reservationPicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER"));

        cancellationDate.setOnClickListener(v ->
                cancellationPicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER"));

        reservationPicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedDate = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            reservationDate.setText(formattedDate);
        });

        cancellationPicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedDate = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            cancellationDate.setText(formattedDate);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupImagePicker() {
        imageContainer = binding.photosContainer;
        binding.uploadButton.setOnClickListener(v -> imageUpload.openGallery(true));
    }

    private void loadCategories() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(List.of(""))
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.addAll(categories.stream().map(Category::getName).toArray(String[]::new));
            adapter.notifyDataSetChanged();
            binding.categorySelector.setAdapter(adapter);
            binding.categorySelector.setTag(categories);
        });
    }

    private void loadEventTypes() {
        eventTypeViewModel.fetchEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            binding.eventTypeRecycleView.setAdapter(new ChecklistAdapter<>(eventTypes));
        });
    }

    private Category getCategory() {
        String categoryName = binding.categorySelector.getSelectedItem().toString();
        Category category;
        if(!categoryName.isEmpty()) {
            category = ((List<Category>) binding.categorySelector.getTag())
                    .stream()
                    .filter(c -> c.getName().equals(categoryName))
                    .findFirst().get();
        } else {
            category = new Category(
                    null,
                    String.valueOf(binding.suggestCategoryNameText.getText()),
                    String.valueOf(binding.suggestCategoryDescriptionText.getText()));
        }
        return category;
    }

    private void createService() {
        CreateServiceRequestDto dto = loadDataFromForm();
        if(dto != null) {
            serviceViewModel.createService(dto).observe(getViewLifecycleOwner(), serviceId -> {
                if (serviceId != null) {
                    if (!imageUris.isEmpty()) {
                        serviceViewModel
                                .uploadImages(serviceId, getContext(), imageUris)
                                .observe(getViewLifecycleOwner(), this::handleUpload);
                    } else {
                        Toast.makeText(
                                requireContext(),
                                R.string.service_created_successfully,
                                Toast.LENGTH_SHORT
                        ).show();
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.action_create_to_serviceOverview, null, new NavOptions.Builder()
                                .setPopUpTo(R.id.createServiceFragment, true)
                                .build());
                    }
                } else {
                    Toast.makeText(
                            requireContext(),
                            R.string.failed_to_create_service,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }
    }

    private CreateServiceRequestDto loadDataFromForm() {
        try {
            Category category = getCategory();
            ReservationType type = binding.manualChecked.isChecked()
                    ? ReservationType.MANUAL
                    : ReservationType.AUTOMATIC;

            List<Float> duration = binding.serviceDuration.getValues();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

            LocalDate cancellationDate = LocalDate.parse(binding.serviceCancellationDeadlineText.getText(), formatter);
            LocalDate reservationDate = LocalDate.parse(binding.serviceReservationDeadlineText.getText(), formatter);

            if(cancellationDate.isBefore(LocalDate.now())) {
                Toast.makeText(
                        requireContext(),
                        R.string.reservation_date_in_past,
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }

            if(reservationDate.isBefore(LocalDate.now())) {
                Toast.makeText(
                        requireContext(),
                        R.string.cancellation_date_in_past,
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }

            if(cancellationDate.isBefore(reservationDate)) {
                Toast.makeText(
                        requireContext(),
                        R.string.cancellation_after_reservation,
                        Toast.LENGTH_LONG
                ).show();
                return null;
            }

            return CreateServiceRequestDto.builder()
                    .name(String.valueOf(binding.serviceNameText.getText()))
                    .description(String.valueOf(binding.serviceDescriptionText.getText()))
                    .price(Double.parseDouble(String.valueOf(binding.servicePriceText.getText())))
                    .discount(Double.parseDouble(String.valueOf(binding.serviceDiscountText.getText())))
                    .specialties(String.valueOf(binding.serviceSpecificitiesText.getText()))
                    .cancellationDeadline(cancellationDate)
                    .reservationDeadline(reservationDate)
                    .minDuration(duration.get(0).intValue())
                    .maxDuration(duration.get(1).intValue())
                    .type(type)
                    .eventTypes(((ChecklistAdapter<EventType>)
                            (Objects.requireNonNull(binding.eventTypeRecycleView.getAdapter())))
                            .getSelectedItems().stream()
                            .map(EventTypeMapper::toRequest)
                            .collect(toList()))
                    .category(new CategoryResponseDto(category.getId(), category.getName(), category.getDescription()))
                    .build();
        } catch (NullPointerException | NumberFormatException | DateTimeParseException exception) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        }
    }

    private void handleUpload(boolean successfulUpload) {
        if(successfulUpload) {
            Toast.makeText(
                    requireContext(),
                    R.string.service_created_successfully,
                    Toast.LENGTH_SHORT
            ).show();
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_create_to_serviceOverview, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.createServiceFragment, true)
                    .build());
        } else {
            Toast.makeText(
                    requireContext(),
                    R.string.failed_to_create_service,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

}