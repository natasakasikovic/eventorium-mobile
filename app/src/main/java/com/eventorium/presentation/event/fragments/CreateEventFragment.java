package com.eventorium.presentation.event.fragments;

import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_ID;
import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_PRIVACY;
import static com.eventorium.presentation.event.fragments.BudgetPlanningFragment.ARG_EVENT_TYPE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentCreateEventBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateEventFragment extends Fragment {

    private EventTypeViewModel eventTypeViewModel;
    private CityViewModel cityViewModel;
    private EventViewModel eventViewModel;
    private FragmentCreateEventBinding binding;
    private final CreateEvent event = new CreateEvent();

    public CreateEventFragment() { }

    public static CreateEventFragment newInstance() {
        return new CreateEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);
        eventViewModel = provider.get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);
        createDatePicker();
        setEventTypes();
        setPrivacy();
        setCities();
        binding.continueButton.setOnClickListener(v -> createEvent());

        return binding.getRoot();
    }

    private TextInputEditText eventDate;
    private void createDatePicker() {
        eventDate = binding.etEventDate;

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select a Date")
                .build();

        eventDate.setOnClickListener(v ->
                datePicker.show(requireActivity().getSupportFragmentManager(), "DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDate selectedDate = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String formatedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
            eventDate.setText(formatedDate);
        });

    }

    private void setEventTypes() {
        EventType all = new EventType();
        all.setName("All");

        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            eventTypes.add(0, all);
            ArrayAdapter<EventType> eventTypeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    eventTypes
            );
            binding.spinnerEventType.setAdapter(eventTypeAdapter);
        });
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

    private void setPrivacy() {
        ArrayAdapter<Privacy> privacyArrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                Privacy.values()
        );
        binding.spinnerPrivacy.setAdapter(privacyArrayAdapter);
    }

    private void createEvent() {
        if (!areFieldsValid()) {
            Toast.makeText(requireContext(), R.string.please_fill_in_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        getFormFields();

        eventViewModel.createEvent(event).observe(getViewLifecycleOwner(), response -> {
            if (response.getData() != null) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                Bundle args = new Bundle();
                args.putLong(ARG_EVENT_ID, response.getData().getId());
                args.putParcelable(ARG_EVENT_TYPE, response.getData().getType());
                args.putParcelable(ARG_EVENT_PRIVACY, response.getData().getPrivacy());
                navController.navigate(R.id.action_create_to_budgetPlanning, args);
            } else {
                Toast.makeText(
                        requireContext(),
                        response.getError(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void getFormFields() {
        EventType selectedType = (EventType) binding.spinnerEventType.getSelectedItem();
        if (selectedType.getName().equals("All")) selectedType = null;
        event.setEventType(selectedType);

        event.setName(binding.etEventName.getText().toString());
        event.setDescription(binding.etDescription.getText().toString());
        event.setAddress(binding.etAddress.getText().toString());
        event.setMaxParticipants(Integer.parseInt(binding.etMaxParticipants.getText().toString()));
        event.setCity((City) binding.spinnerCity.getSelectedItem());
        event.setPrivacy((Privacy) binding.spinnerPrivacy.getSelectedItem());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        event.setDate(LocalDate.parse(binding.etEventDate.getText(), formatter));
    }

    private boolean areFieldsValid() {
        return !TextUtils.isEmpty(binding.etEventName.getText()) &&
                !TextUtils.isEmpty(binding.etDescription.getText()) &&
                !TextUtils.isEmpty(binding.etMaxParticipants.getText()) &&
                !TextUtils.isEmpty(binding.etAddress.getText()) &&
                !TextUtils.isEmpty(binding.etEventDate.getText());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}