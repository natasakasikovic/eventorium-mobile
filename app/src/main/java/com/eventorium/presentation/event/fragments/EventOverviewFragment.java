package com.eventorium.presentation.event.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.databinding.FragmentEventOverviewBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;
import com.eventorium.presentation.event.viewmodels.EventViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EventOverviewFragment extends Fragment {

    private FragmentEventOverviewBinding binding;
    private EventViewModel viewModel;

    public EventOverviewFragment() { }

    public static EventOverviewFragment newInstance() {
        return new EventOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            binding.eventsRecycleView.setAdapter(new EventsAdapter(events));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}