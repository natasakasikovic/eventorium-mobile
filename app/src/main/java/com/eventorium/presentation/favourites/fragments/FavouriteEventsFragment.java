package com.eventorium.presentation.favourites.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.databinding.FragmentFavouriteEventsBinding;
import com.eventorium.presentation.event.adapters.EventsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouriteEventsFragment extends Fragment {

    private FragmentFavouriteEventsBinding binding;
    private static final List<EventSummary> events = new ArrayList<>();

    public FavouriteEventsFragment() { }

    public static FavouriteEventsFragment newInstance() {
        return new FavouriteEventsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouriteEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareEventData();
        binding.eventsRecycleView.setAdapter(new EventsAdapter(events, event -> {

        }));
    }

    public void prepareEventData() {
        events.clear();
    }
}