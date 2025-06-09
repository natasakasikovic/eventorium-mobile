package com.eventorium.presentation.event.fragments;

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
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.EditableEvent;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.UpdateEvent;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentEditEventBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.event.viewmodels.EventViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditEventFragment extends Fragment {

    private FragmentEditEventBinding binding;
    public static String ARG_EVENT_ID = "event_id";
    private EventViewModel viewModel;
    private Long eventId;
    private EditableEvent event;

    private CityViewModel cityViewModel;
    private ArrayAdapter<City> cityArrayAdapter;
    private EventTypeViewModel eventTypeViewModel;
    private ArrayAdapter<EventType> eventTypeAdapter;

    private boolean eventLoaded = false;
    private boolean eventTypesLoaded = false;


    public EditEventFragment() { }

    public static EditEventFragment newInstance(Long id) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        eventId = getArguments().getLong(ARG_EVENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditEventBinding.inflate(inflater, container, false);
        setViewModels();
        createDatePicker();
        setupSaveButton();
        cityArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        eventTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadEvent();
        loadCities();
        loadEventTypes();
    }

    private void setViewModels() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(EventViewModel.class);
        cityViewModel = provider.get(CityViewModel.class);
        eventTypeViewModel = provider.get(EventTypeViewModel.class);
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
            String formatedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            eventDate.setText(formatedDate);
        });

    }

    private void setupSaveButton() {
        MaterialButton saveBtn = binding.saveButton;
        saveBtn.setOnClickListener(v -> {
            viewModel.updateEvent(eventId, getFormFields()).observe(getViewLifecycleOwner(), result -> {
                if (result.getError() == null) {
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                    navController.popBackStack();
                } else {
                    Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadEvent() {
        viewModel.getEditableEvent(eventId).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                event = result.getData();
                eventLoaded = true;
                maybeFillForm();
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEventTypes() {
        EventType all = new EventType();
        all.setName("All");

        eventTypeViewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            eventTypes.add(0, all);
            eventTypeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    eventTypes
            );
            binding.spinnerEventType.setAdapter(eventTypeAdapter);
            eventTypesLoaded = true;
            maybeFillForm();
        });
    }

    private void maybeFillForm() {
        if (eventLoaded && eventTypesLoaded) {
            fillForm();
        }
    }

    private void loadCities() {
        cityViewModel.getCities().observe(getViewLifecycleOwner(), cities -> {
            cityArrayAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    cities
            );
            binding.spinnerCity.setAdapter(cityArrayAdapter);
        });
    }

    private void fillForm() {
        // simple info
        binding.etEventName.setText(event.getName());
        binding.etDescription.setText(event.getDescription());
        binding.etMaxParticipants.setText(event.getMaxParticipants().toString());
        binding.etAddress.setText(event.getAddress());

        // city
        City city = event.getCity();
        if (cityArrayAdapter != null && city != null) {
            int position = cityArrayAdapter.getPosition(city);
            binding.spinnerCity.setSelection(position);
        }

        // event type
        if (event.getType() == null) binding.spinnerEventType.setSelection(0);
        else binding.spinnerEventType.setSelection(eventTypeAdapter.getPosition(event.getType()));

        // event date
        String formattedDate = event.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        eventDate.setText(formattedDate);
    }

    private UpdateEvent getFormFields() {
        UpdateEvent updated = new UpdateEvent();

        EventType selectedType = (EventType) binding.spinnerEventType.getSelectedItem();
        if (selectedType.getName().equals("All")) selectedType = null;
        updated.setEventType(selectedType);

        updated.setName(binding.etEventName.getText().toString());
        updated.setDescription(binding.etDescription.getText().toString());
        updated.setAddress(binding.etAddress.getText().toString());
        updated.setMaxParticipants(Integer.parseInt(binding.etMaxParticipants.getText().toString()));
        updated.setCity((City) binding.spinnerCity.getSelectedItem());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updated.setDate(LocalDate.parse(binding.etEventDate.getText(), formatter));

        return updated;
    }
}