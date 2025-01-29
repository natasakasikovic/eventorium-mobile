package com.eventorium.presentation.calendar.fragments;

import static com.eventorium.presentation.event.fragments.EventDetailsFragment.ARG_EVENT_ID;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.eventorium.R;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.databinding.FragmentDateDetailsBottomSheetBinding;
import com.eventorium.presentation.calendar.adapters.CalendarEventsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DateDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentDateDetailsBottomSheetBinding binding;

    public static DateDetailsBottomSheetFragment newInstance(List<CalendarEvent> attendingEvents) {
        DateDetailsBottomSheetFragment fragment = new DateDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("attending", new ArrayList<>(attendingEvents));
        fragment.setArguments(args);
        return fragment;
    }

    public static DateDetailsBottomSheetFragment newInstance(List<CalendarEvent> attendingEvents,
                                                             List<CalendarEvent> organizedEvents) {
        DateDetailsBottomSheetFragment fragment = new DateDetailsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("attending", new ArrayList<>(attendingEvents));
        args.putParcelableArrayList("organized", new ArrayList<>(organizedEvents));
        fragment.setArguments(args);
        return fragment;
    }

    public DateDetailsBottomSheetFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDateDetailsBottomSheetBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            loadAttendingEvents();
            loadOrganizedEvents();
        }
        return binding.getRoot();
    }

    private void loadAttendingEvents() {
        List<CalendarEvent> events = getArguments().getParcelableArrayList("attending");
        if (events != null && !events.isEmpty()) {
            CalendarEventsAdapter adapter = displayEvents(events);
            binding.attendingEvents.setAdapter(adapter);
        }
        else {
            binding.noEventsText.setVisibility(View.VISIBLE);
            binding.eventsTitle.setVisibility(View.GONE);
        }
    }

    private void loadOrganizedEvents() {
        List<CalendarEvent> events = getArguments().getParcelableArrayList("organized");
        if (events != null && !events.isEmpty()) {
            CalendarEventsAdapter adapter = displayEvents(events);
            binding.organizedEvents.setAdapter(adapter);
        }
        else {
            binding.organizedEventsTitle.setVisibility(View.GONE);
        }
    }

    private CalendarEventsAdapter displayEvents(List<CalendarEvent> events) {
        return new CalendarEventsAdapter(events, event -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
            Bundle args = new Bundle();
            args.putLong(ARG_EVENT_ID, event.getId());
            navController.navigate(R.id.action_calendar_to_event_details, args);
        });
    }
}