package com.eventorium.presentation.solution.fragments.service;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.event.models.EventType;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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
        loadEventTypes();

        return binding.getRoot();
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
            String selectedDate = new SimpleDateFormat("dd.MM.yyyy")
                    .format(new Date(selection));
            reservationDate.setText(selectedDate);
        });

        cancellationPicker.addOnPositiveButtonClickListener(selection -> {
            String selectedDate = new SimpleDateFormat("dd.MM.yyyy")
                    .format(new Date(selection));
            cancellationDate.setText(selectedDate);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}