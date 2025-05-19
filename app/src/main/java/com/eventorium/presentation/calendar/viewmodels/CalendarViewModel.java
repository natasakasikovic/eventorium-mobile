package com.eventorium.presentation.calendar.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.solution.models.service.CalendarReservation;
import com.eventorium.data.solution.repositories.ServiceRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalendarViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;

    @Inject
    public CalendarViewModel(EventRepository eventRepository, ServiceRepository serviceRepository) {
        this.eventRepository = eventRepository;
        this.serviceRepository = serviceRepository;
    }

    public LiveData<Result<List<CalendarEvent>>> getAttendingEvents() {
        return eventRepository.getAttendingEvents();
    }

    public LiveData<Result<List<CalendarEvent>>> getOrganizerEvents() {
        return eventRepository.getOrganizerEvents();
    }

    public LiveData<Result<List<CalendarReservation>>> getReservations() {
        return serviceRepository.getReservations();
    }
}
