package com.eventorium.presentation.solution.fragments.service;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.databinding.FragmentReserveServiceBinding;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.solution.viewmodels.ReservationViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReserveServiceFragment extends Fragment {

    public static final String ARG_SERVICE_ID = "SERVICE_ID";
    public static final String ARG_PLANNED_AMOUNT = "PLANNED_AMOUNT";
    public static final String ARG_EVENT_ID = "EVENT_ID";

    private Long serviceId;
    private Double plannedAmount;
    private Long eventId;

    private FragmentReserveServiceBinding binding;
    private ReservationViewModel viewModel;
    private EventViewModel eventViewModel;


    public ReserveServiceFragment() {}

    public static ReserveServiceFragment newInstance(Long serviceId) {
        ReserveServiceFragment fragment = new ReserveServiceFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SERVICE_ID, serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReserveServiceFragment newInstance(Long serviceId, Long eventId, Double plannedAmount) {
        ReserveServiceFragment fragment = new ReserveServiceFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PLANNED_AMOUNT, plannedAmount);
        args.putLong(ARG_EVENT_ID, eventId);
        args.putLong(ARG_SERVICE_ID, serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null)
            return;

        Bundle args = getArguments();
        serviceId = args.getLong(ARG_SERVICE_ID);
        plannedAmount = args.containsKey(ARG_PLANNED_AMOUNT) ? args.getDouble(ARG_PLANNED_AMOUNT) : 0.0;
        eventId = args.containsKey(ARG_EVENT_ID) ? args.getLong(ARG_EVENT_ID) : 0;

        initViewModels();
    }

    private void initViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ReservationViewModel.class);
        eventViewModel = provider.get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReserveServiceBinding.inflate(inflater, container, false);
        createTimePicker();

        if (plannedAmount != 0.0) {
            binding.eventSelectionInputLayout.setVisibility(View.GONE);
            binding.plannedAmountInputLayout.setVisibility(View.GONE);
        } else
            loadFutureEventsIntoSpinner();

        binding.reserveServiceButton.setOnClickListener(v -> reserveService());
        return binding.getRoot();
    }

    private void createTimePicker() {
        setTimePickerListener(binding.timePickerTextFrom);
        setTimePickerListener(binding.timePickerTextTo);
    }

    private void loadFutureEventsIntoSpinner() {
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventViewModel.getFutureEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                adapter.addAll(result.getData());
                adapter.notifyDataSetChanged();
                binding.eventSelector.setAdapter(adapter);
            }
        });
    }

    private void setTimePickerListener(TextInputEditText timeField) {
        timeField.setOnClickListener(v -> {

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minuteOfHour) -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minuteOfHour);

                        String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(calendar.getTime());

                        timeField.setText(time);
                    }, 0, 0, false
            );

            timePickerDialog.show();
        });
    }

    private void reserveService() {
        if (!validateInput()) return;

        String startingTime = getTimeFromInput(binding.timePickerTextFrom);
        String endingTime = getTimeFromInput(binding.timePickerTextTo);

        if (plannedAmount == 0.0)
            plannedAmount = parsePlannedAmount(binding.plannedAmount);

        if (eventId == 0) {
            Event event = (Event) binding.eventSelector.getSelectedItem();
            if (event == null) {
                Toast.makeText(requireContext(), "Please select an event.", Toast.LENGTH_SHORT).show();
                return;
            }
            eventId = event.getId();
        }

        viewModel.reserveService(startingTime, endingTime, plannedAmount, eventId, serviceId)
                .observe(getViewLifecycleOwner(), result -> {
                    if (result.getError() == null)
                        showSuccessMessageAndNavigate();
                    else
                        Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateInput() {
        if (binding.timePickerTextFrom.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select a starting time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.timePickerTextTo.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select an ending time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (plannedAmount == null && binding.plannedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a planned amount.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getTimeFromInput(EditText timeInput) {
        return timeInput.getText().toString().toUpperCase();
    }

    private Double parsePlannedAmount(EditText input) {
        try {
            return Double.parseDouble(input.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid planned amount.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void showSuccessMessageAndNavigate() {
        Toast.makeText(requireContext(), "Successfully reserved!", Toast.LENGTH_SHORT).show();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        navController.popBackStack();
    }
}