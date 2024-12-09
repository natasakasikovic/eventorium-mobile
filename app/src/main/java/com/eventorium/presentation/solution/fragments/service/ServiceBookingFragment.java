package com.eventorium.presentation.solution.fragments.service;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.databinding.FragmentServiceBookingBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceBookingFragment extends Fragment {

    private FragmentServiceBookingBinding binding;
    private TextInputEditText bookingDate;

    public ServiceBookingFragment() {}

    public static ServiceBookingFragment newInstance() { return new ServiceBookingFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServiceBookingBinding.inflate(inflater, container, false);
        createDatePicker();
        createTimePicker();
        return binding.getRoot();
    }

    private void createTimePicker() {
        setTimePickerListener(binding.timePickerTextFrom);
        setTimePickerListener(binding.timePickerTextTo);
    }

    private void setTimePickerListener(TextInputEditText timeField) {
        timeField.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, selectedHour, selectedMinute) -> {
                        String formattedTime = formatTime(selectedHour, selectedMinute);
                        timeField.setText(formattedTime);
                    }, hour, minute, true );
            timePickerDialog.show();
        });
    }

    private void createDatePicker() {
        bookingDate = binding.serviceDateText;

        MaterialDatePicker<Long> bookingPicker = MaterialDatePicker.Builder.datePicker().setTitleText(getString(R.string.select_date)).build();

        bookingDate.setOnClickListener(v ->
                bookingPicker.show(requireActivity().getSupportFragmentManager(), "SERVICE_BOOKING_DATE_PICKER")
        );

        bookingPicker.addOnPositiveButtonClickListener(selection -> {
            String selectedDate = formatDate(selection);
            bookingDate.setText(selectedDate);
        });
    }

    private String formatTime(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }
    private String formatDate(Long selection) {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(selection));
    }

}
