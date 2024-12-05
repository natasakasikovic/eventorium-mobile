package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.dtos.eventtypes.EventTypeRequestDto;
import com.eventorium.data.models.EventType;
import com.eventorium.data.repositories.EventTypeRepository;

public class EventTypeViewModel extends ViewModel {
    private final EventTypeRepository eventTypeRepository;

    public EventTypeViewModel(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public LiveData<EventType> createEventType(EventTypeRequestDto eventTypeDto) {
        return eventTypeRepository.createEventType(eventTypeDto);
    }
}
