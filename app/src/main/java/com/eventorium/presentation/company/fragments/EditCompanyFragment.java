package com.eventorium.presentation.company.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.data.company.models.Company;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentEditCompanyBinding;
import com.eventorium.presentation.company.viewmodels.CompanyViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditCompanyFragment extends Fragment {

    private FragmentEditCompanyBinding binding;
    private ArrayAdapter<City> cityArrayAdapter;
    private CompanyViewModel viewModel;
    private CityViewModel cityViewModel;
    private Long id;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditCompanyBinding.inflate(inflater, container, false);
        binding.saveButton.setOnClickListener(v -> save());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
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
                // TODO: update images
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}