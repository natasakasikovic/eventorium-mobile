package com.eventorium.presentation.calendar.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.databinding.FragmentCalendarBinding;
import com.eventorium.presentation.auth.viewmodels.AuthViewModel;
import com.eventorium.presentation.calendar.utils.EventDecorator;
import com.eventorium.presentation.calendar.viewmodels.CalendarViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private CalendarViewModel viewModel;
    private AuthViewModel authViewModel;

    private List<CalendarEvent> attendingEvents;
    private List<CalendarEvent> organizerEvents;
    private MaterialCalendarView calendarView;

    public CalendarFragment() { }

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(CalendarViewModel.class);
        authViewModel = provider.get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        setupCalendarView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getAttendingEvents();
        getOrganizerEvents();
    }

    private void setupCalendarView() {
        calendarView = binding.calendarView;
        calendarView.setOnDateChangedListener((widget, date, selected) -> onDateSelected(date, selected));
    }

    private void onDateSelected(CalendarDay date, boolean selected) {
        if (selected) {
            List<CalendarEvent> events = filterEvents(attendingEvents, date);
            DateDetailsBottomSheetFragment details;
            if (authViewModel.getUserRole().equals("EVENT_ORGANIZER")) {
                List<CalendarEvent> filteredEvents = filterEvents(organizerEvents, date);
                details = DateDetailsBottomSheetFragment.newInstance(events, filteredEvents);
            } else {
                details = DateDetailsBottomSheetFragment.newInstance(events);
            }
            details.show(getChildFragmentManager(), details.getTag());
        }
    }

    private List<CalendarEvent> filterEvents(List<CalendarEvent> events, CalendarDay calendarDate) {
        if (events == null) {
            return List.of();
        }

        LocalDate date = LocalDate.of(calendarDate.getYear(), calendarDate.getMonth() + 1, calendarDate.getDay());
        return events.stream()
                .filter(event -> event.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private void getAttendingEvents() {
        viewModel.getAttendingEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                attendingEvents = result.getData();
                markDates(attendingEvents, Color.BLUE, Color.GRAY);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOrganizerEvents() {
        if (!authViewModel.getUserRole().equals("EVENT_ORGANIZER")) return;
        viewModel.getOrganizerEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getError() == null) {
                organizerEvents = result.getData();
                markDates(organizerEvents, Color.BLUE, Color.WHITE);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markDates(List<CalendarEvent> events, int foregroundColor, int backgroundColor) {
        for (CalendarEvent event: events) {
            CalendarDay eventDate = CalendarDay.from(event.getDate().getYear(),
                                                     event.getDate().getMonthValue() - 1,
                                                     event.getDate().getDayOfMonth());
            calendarView.addDecorator(new EventDecorator(foregroundColor, backgroundColor, eventDate));
        }
    }

}