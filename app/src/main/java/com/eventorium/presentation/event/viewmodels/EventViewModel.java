package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {

    private final EventRepository repository;

    @Inject
    public EventViewModel(EventRepository eventRepository) {
        this.repository = eventRepository;
    }

    public LiveData<Result<List<EventSummary>>> getEvents(){
        return repository.getEvents();
    }

    public LiveData<Result<Event>> createEvent(CreateEvent event) {
        return repository.createEvent(event);
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        return repository.searchEvents(keyword);
    }
}