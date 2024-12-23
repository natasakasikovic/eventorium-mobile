package com.eventorium.presentation.solution.fragments.service;

import static java.util.stream.Collectors.toList;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.category.dtos.CategoryResponseDto;
import com.eventorium.data.event.mappers.EventTypeMapper;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.solution.dtos.UpdateServiceRequestDto;
import com.eventorium.data.solution.models.ServiceSummary;
import com.eventorium.data.util.models.ReservationType;
import com.eventorium.databinding.FragmentEditServiceBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.solution.viewmodels.ManageableServiceViewModel;
import com.eventorium.presentation.solution.viewmodels.ServiceViewModel;
import com.eventorium.presentation.util.adapters.ChecklistAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditServiceFragment extends Fragment {

    private FragmentEditServiceBinding binding;

    private ServiceViewModel serviceViewModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private ChecklistAdapter<EventType> adapter;

    private static final String ARG_SERVICE = "serviceSummary";
    private ServiceSummary serviceSummary;
    private EventTypeViewModel eventTypeViewModel;

    public EditServiceFragment() {
    }

    public static EditServiceFragment newInstance(ServiceSummary serviceSummary) {
        EditServiceFragment fragment = new EditServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SERVICE, serviceSummary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            serviceSummary = getArguments().getParcelable(ARG_SERVICE);
        }
        ViewModelProvider provider = new ViewModelProvider(this);
        serviceViewModel = provider.get(ServiceViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditServiceBinding.inflate(inflater, container, false);
        binding.editServiceButton.setOnClickListener(v -> editService());
        loadEventTypes();

        return binding.getRoot();
    }

    private void editService() {
        UpdateServiceRequestDto dto = loadDataFromForm();
        if(dto != null) {
            serviceViewModel.updateService(serviceSummary.getId(), loadDataFromForm())
                    .observe(getViewLifecycleOwner(), service -> {
                        if (service != null) {
                            Toast.makeText(
                                    requireContext(),
                                    R.string.service_updated_successfully,
                                    Toast.LENGTH_SHORT
                            ).show();
                            NavController navController = Navigation.findNavController(requireView());
                            navController.navigate(R.id.action_update_to_serviceManagement);
                        } else {
                            Toast.makeText(
                                    requireContext(),
                                    R.string.failed_to_update_service,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        }
    }

    private UpdateServiceRequestDto loadDataFromForm() {
        try {
            ReservationType type = binding.manualChecked.isChecked()
                    ? ReservationType.MANUAL
                    : ReservationType.AUTOMATIC;

            List<Float> duration = binding.serviceDuration.getValues();

            LocalDate cancellationDate = LocalDate.parse(binding.serviceCancellationDeadlineText.getText(), formatter);
            LocalDate reservationDate = LocalDate.parse(binding.serviceReservationDeadlineText.getText(), formatter);

            return UpdateServiceRequestDto.builder()
                    .name(String.valueOf(binding.serviceNameEditText.getText()))
                    .description(String.valueOf(binding.serviceDescriptionText.getText()))
                    .price(Double.parseDouble(String.valueOf(binding.servicePriceText.getText())))
                    .discount(Double.parseDouble(String.valueOf(binding.serviceDiscountText.getText())))
                    .specialties(String.valueOf(binding.serviceSpecificitiesText.getText()))
                    .cancellationDeadline(cancellationDate)
                    .available(binding.availabilityBox.isChecked())
                    .visible(binding.visibilityBox.isChecked())
                    .reservationDeadline(reservationDate)
                    .minDuration(duration.get(0).intValue())
                    .maxDuration(duration.get(1).intValue())
                    .type(type)
                    .eventTypesIds(((ChecklistAdapter<EventType>)
                            (Objects.requireNonNull(binding.eventTypeRecycleView.getAdapter())))
                            .getSelectedItems().stream()
                            .map(EventType::getId)
                            .collect(toList()))
                    .build();
        } catch (NullPointerException | NumberFormatException exception) {
            Toast.makeText(
                    requireContext(),
                    R.string.please_fill_in_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return null;
        }
    }

    @SuppressLint("SetTextI18n")
    private void fillForm() {
        serviceViewModel.getService(serviceSummary.getId()).observe(getViewLifecycleOwner(), service -> {
            binding.serviceNameEditText.setText(service.getName());
            binding.serviceDescriptionText.setText(service.getDescription());
            binding.serviceDiscountText.setText(service.getDiscount().toString());
            binding.serviceSpecificitiesText.setText(service.getSpecialties());
            binding.serviceReservationDeadlineText.setText(service.getReservationDeadline().format(formatter));
            binding.serviceCancellationDeadlineText.setText(service.getCancellationDeadline().format(formatter));
            binding.servicePriceText.setText(service.getPrice().toString());
            binding.visibilityBox.setChecked(service.getVisible());
            binding.availabilityBox.setChecked(service.getAvailable());
            binding.serviceDuration.setValues(List.of((float)service.getMinDuration(), (float)service.getMaxDuration()));
            if(service.getType() == ReservationType.AUTOMATIC) {
                binding.manualChecked.setChecked(true);
            } else {
                binding.automaticChecked.setChecked(true);
            }
            for(EventType eventType : service.getEventTypes()) {
                adapter.selectItem(eventType.getName());
            }
        });
    }

    private void loadEventTypes() {
        eventTypeViewModel.fetchEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter = new ChecklistAdapter<>(eventTypes);
            binding.eventTypeRecycleView.setAdapter(adapter);
            createDatePickers();
            fillForm();
        });
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
}