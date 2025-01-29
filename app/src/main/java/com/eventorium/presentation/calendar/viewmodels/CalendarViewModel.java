package com.eventorium.presentation.calendar.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CalendarViewModel extends ViewModel {
    private final EventRepository eventRepository;

    @Inject
    public CalendarViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public LiveData<Result<List<CalendarEvent>>> getAttendingEvents() {
        return eventRepository.getAttendingEvents();
    }

    public LiveData<Result<List<CalendarEvent>>> getOrganizerEvents() {
        return eventRepository.getOrganizerEvents();
    }

}
