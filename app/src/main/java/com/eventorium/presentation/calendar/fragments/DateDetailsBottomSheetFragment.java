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
import com.eventorium.data.solution.models.service.CalendarReservation;
import com.eventorium.databinding.FragmentDateDetailsBottomSheetBinding;
import com.eventorium.presentation.calendar.adapters.CalendarEventsAdapter;
import com.eventorium.presentation.calendar.adapters.CalendarReservationsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DateDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentDateDetailsBottomSheetBinding binding;

    public static DateDetailsBottomSheetFragment newInstance(List<CalendarEvent> attendingEvents,
                                                             List<CalendarEvent> organizedEvents,
                                                             List<CalendarReservation> reservations) {
        DateDetailsBottomSheetFragment fragment = new DateDetailsBottomSheetFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList("attending", new ArrayList<>(attendingEvents));
        if (organizedEvents != null)
            args.putParcelableArrayList("organized", new ArrayList<>(organizedEvents));
        if (reservations != null)
            args.putParcelableArrayList("reservations", new ArrayList<>(reservations));

        fragment.setArguments(args);
        return fragment;
    }

    public DateDetailsBottomSheetFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDateDetailsBottomSheetBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            loadAttendingEvents();
            loadOrganizedEvents();
            loadReservations();
        }
        return binding.getRoot();
    }

    private void loadAttendingEvents() {
        List<CalendarEvent> events = getArguments().getParcelableArrayList("attending");
        if (events != null && !events.isEmpty()) {
            CalendarEventsAdapter adapter = new CalendarEventsAdapter(events, this::navigateToEventDetails);
            binding.attendingEvents.setAdapter(adapter);
        } else {
            binding.noEventsText.setVisibility(View.VISIBLE);
            binding.eventsTitle.setVisibility(View.GONE);
        }
    }

    private void loadOrganizedEvents() {
        List<CalendarEvent> events = getArguments().getParcelableArrayList("organized");
        if (events != null && !events.isEmpty()) {
            binding.organizedEventsTitle.setVisibility(View.VISIBLE);
            CalendarEventsAdapter adapter = new CalendarEventsAdapter(events, this::navigateToEventDetails);
            binding.organizedEvents.setAdapter(adapter);
        }
    }

    private void loadReservations() {
        List<CalendarReservation> reservations = getArguments().getParcelableArrayList("reservations");
        if (reservations != null && !reservations.isEmpty()) {
            binding.reservationsTitle.setVisibility(View.VISIBLE);
            binding.reservations.setAdapter(new CalendarReservationsAdapter(reservations));
        }
    }

    private void navigateToEventDetails(CalendarEvent event) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, event.getId());
        navController.navigate(R.id.action_calendar_to_event_details, args);
    }
}
