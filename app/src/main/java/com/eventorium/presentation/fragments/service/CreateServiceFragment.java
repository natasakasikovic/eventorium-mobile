package com.eventorium.presentation.fragments.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.eventorium.R;
import com.eventorium.databinding.FragmentCreateServiceBinding;
import com.eventorium.presentation.adapters.ChecklistAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateServiceFragment extends Fragment {

    private FragmentCreateServiceBinding binding;

    public CreateServiceFragment() {
    }

    public static CreateServiceFragment newInstance() {
        return new CreateServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateServiceBinding.inflate(inflater, container, false);
        setupCategoryAutoCompleteAdapter();
        createDatePickers();
        binding.categoryRecycleView.setAdapter(new ChecklistAdapter(List.of(
                "Wedding",
                "Birthday Party",
                "Conference",
                "Concert",
                "Corporate Event",
                "Workshop",
                "Fundraiser",
                "Networking Event",
                "Exhibition",
                "Festival",
                "Sports Event"
        )));
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

    private void setupCategoryAutoCompleteAdapter() {
        // TODO: Replace the hardcoded list of cities with a dynamic list
        List<String> serviceCategories = List.of(
                "Venue Services",
                "Catering and Food Services",
                "Entertainment",
                "Music",
                "Audio-Visual and Lighting",
                "Photography and Videography",
                "Planning and Coordination"
        );

        AutoCompleteTextView categoryAutocomplete = binding.categorySelector;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                serviceCategories);
        categoryAutocomplete.setAdapter(adapter);

        categoryAutocomplete.setThreshold(0);
    }
}