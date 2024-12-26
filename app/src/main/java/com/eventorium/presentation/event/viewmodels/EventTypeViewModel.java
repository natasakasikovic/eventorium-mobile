package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.repositories.EventTypeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventTypeViewModel extends ViewModel {
    private final EventTypeRepository eventTypeRepository;

    @Inject
    public EventTypeViewModel(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public LiveData<EventType> createEventType(CreateEventType eventType) {
        return eventTypeRepository.createEventType(eventType);
    }

    public LiveData<List<EventType>> fetchEventTypes() {
        return eventTypeRepository.getEventTypes();
    }
}
