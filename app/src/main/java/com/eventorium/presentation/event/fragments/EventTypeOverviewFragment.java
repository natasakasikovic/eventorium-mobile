package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eventorium.R;
import com.eventorium.data.event.models.EventType;
import com.eventorium.databinding.FragmentEventTypeOverviewBinding;
import com.eventorium.presentation.event.adapters.EventTypesAdapter;
import com.eventorium.presentation.event.viewmodels.EventTypeViewModel;
import com.eventorium.presentation.util.listeners.OnEditClickListener;


import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventTypeOverviewFragment extends Fragment {

    private FragmentEventTypeOverviewBinding binding;
    private EventTypeViewModel viewModel;
    private EventTypesAdapter adapter;
    private TextView noEventTypesText;
    private List<EventType> eventTypes;

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
            public void onEditClick(EventType eventType) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                navController.navigate(R.id.action_event_types_overview_to_edit,
                        EditEventTypeFragment.newInstance(eventType).getArguments());
            }

            @Override
            public void onDeleteClick(EventType eventType) {
                deleteEventType(eventType.getId());
            }
        });

        noEventTypesText = binding.noEventTypesText;
        binding.eventTypesRecycleView.setAdapter(adapter);
        loadEventTypes();
    }

    private void deleteEventType(Long id) {
        viewModel.delete(id).observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                Toast.makeText(requireContext(), R.string.event_type_deleted_successfully, Toast.LENGTH_SHORT).show();
                adapter.removeEventTypeById(id);
                if (eventTypes.isEmpty()) noEventTypesText.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEventTypes() {
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            if (eventTypes.isEmpty()) noEventTypesText.setVisibility(View.VISIBLE);
            adapter.setEventTypes(eventTypes);
            this.eventTypes = eventTypes;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}