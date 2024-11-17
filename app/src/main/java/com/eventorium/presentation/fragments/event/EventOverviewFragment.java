package com.eventorium.presentation.fragments.event;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.R;
import com.eventorium.data.models.Event;
import com.eventorium.databinding.FragmentEventOverviewBinding;
import com.eventorium.presentation.adapters.EventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventOverviewFragment extends Fragment {

    private FragmentEventOverviewBinding binding;
    private static final List<Event> events = new ArrayList<>();

    public EventOverviewFragment() { }

    public static EventOverviewFragment newInstance() {
        return new EventOverviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        prepareEventData();
        binding.eventsRecycleView.setAdapter(new EventsAdapter(events));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    public void prepareEventData() {
        events.clear();
        events.add(new Event("Concert",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Conference",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Workshop",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Festival",  "Novi Sad", R.drawable.conference));
        events.add(new Event("Webinar", "Novi Sad", R.drawable.conference));
        events.add(new Event("Webinar", "Novi Sad", R.drawable.conference));
        events.add(new Event("Webinar", "Novi Sad", R.drawable.conference));
    }

}