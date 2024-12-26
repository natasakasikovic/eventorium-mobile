package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.models.Privacy;
import com.eventorium.data.shared.models.City;
import com.eventorium.databinding.FragmentCreateEventBinding;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.shared.viewmodels.CityViewModel;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateEventFragment extends Fragment {

    private EventTypeViewModel eventTypeViewModel;
    private CityViewModel cityViewModel;
    private FragmentCreateEventBinding binding;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateEventBinding.inflate(inflater, container, false);

        setEventTypes();
        setPrivacy();
        setCities();
        return binding.getRoot();
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
}