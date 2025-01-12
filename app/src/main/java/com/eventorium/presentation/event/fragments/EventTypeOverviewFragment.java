package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentEventTypeOverviewBinding;
import com.eventorium.presentation.event.adapters.EventTypesAdapter;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.util.listeners.OnEditClickListener;


import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventTypeOverviewFragment extends Fragment {

    private FragmentEventTypeOverviewBinding binding;
    private EventTypeViewModel viewModel;
    private EventTypesAdapter adapter;

    public EventTypeOverviewFragment() {
    }

    public static EventTypeOverviewFragment newInstance() {
        return new EventTypeOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventTypeOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new EventTypesAdapter(new ArrayList<>(), new OnEditClickListener<>() {

            @Override
            public void onEditClick(EventType item) {
                Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(EventType item) {
                Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        binding.eventTypesRecycleView.setAdapter(adapter);
        loadEventTypes();
    }

    private void loadEventTypes() {
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            adapter.setEventTypes(eventTypes);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}