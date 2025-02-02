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
import com.eventorium.data.solution.models.service.CalendarReservation;
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
    private List<CalendarReservation> reservations;
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
        getReservations();
    }

    private void setupCalendarView() {
        calendarView = binding.calendarView;
        calendarView.setOnDateChangedListener((widget, date, selected) -> onDateSelected(date, selected));
    }

    private void onDateSelected(CalendarDay date, boolean selected) {
        if (!selected) return;

        List<CalendarEvent> events = filterEvents(attendingEvents, date);
        List<CalendarEvent> organizerEvents = filterEvents(this.organizerEvents, date);
        List<CalendarReservation> reservations = filterReservations(date);

        DateDetailsBottomSheetFragment details = createDetailsFragment(events, organizerEvents, reservations);
        details.show(getChildFragmentManager(), details.getTag());
    }

    private DateDetailsBottomSheetFragment createDetailsFragment(List<CalendarEvent> events,
                                                                 List<CalendarEvent> organizerEvents,
                                                                 List<CalendarReservation> reservations) {
        if (authViewModel.getUserRole().equals("EVENT_ORGANIZER")) {
            return DateDetailsBottomSheetFragment.newInstance(events, organizerEvents, null);
        } else if (authViewModel.getUserRole().equals("PROVIDER")) {
            return DateDetailsBottomSheetFragment.newInstance(events, null, reservations);
        } else {
            return DateDetailsBottomSheetFragment.newInstance(events, null, null);
        }
    }

    private List<CalendarEvent> filterEvents(List<CalendarEvent> events, CalendarDay calendarDate) {
        if (events == null) return List.of();

        LocalDate date = LocalDate.of(calendarDate.getYear(), calendarDate.getMonth() + 1, calendarDate.getDay());
        return events.stream()
                .filter(event -> event.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private List<CalendarReservation> filterReservations(CalendarDay calendarDate) {
        if (reservations == null) return List.of();

        LocalDate date = LocalDate.of(calendarDate.getYear(), calendarDate.getMonth() + 1, calendarDate.getDay());
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .collect(Collectors.toList());
    }

    private void getAttendingEvents() {
        viewModel.getAttendingEvents().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                attendingEvents = result.getData();
                markDates(attendingEvents, Color.GRAY);
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
                markDates(organizerEvents, Color.WHITE);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReservations() {
        if (!authViewModel.getUserRole().equals("PROVIDER")) return;
        viewModel.getReservations().observe(getViewLifecycleOwner(), result -> {
            if (result.getData() != null) {
                reservations = result.getData();
                markDates(reservations);
            } else {
                Toast.makeText(requireContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markDates(List<CalendarEvent> events, int backgroundColor) {
        for (CalendarEvent event: events) {
            CalendarDay eventDate = CalendarDay.from(event.getDate().getYear(),
                                                     event.getDate().getMonthValue() - 1,
                                                     event.getDate().getDayOfMonth());
            calendarView.addDecorator(new EventDecorator(Color.MAGENTA, backgroundColor, eventDate));
        }
    }

    private void markDates(List<CalendarReservation> reservations) {
        for (CalendarReservation reservation: reservations) {
            CalendarDay reservationDate = CalendarDay.from(reservation.getDate().getYear(),
                                               reservation.getDate().getMonthValue() - 1,
                                                     reservation.getDate().getDayOfMonth());
            calendarView.addDecorator(new EventDecorator(Color.DKGRAY, Color.LTGRAY, reservationDate));
        }
    }

}