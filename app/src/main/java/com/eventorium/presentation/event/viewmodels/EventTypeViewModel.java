package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.dtos.EventTypeRequestDto;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.repositories.EventTypeRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventTypeViewModel extends ViewModel {
    private final EventTypeRepository eventTypeRepository;

    @Inject
    public EventTypeViewModel(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    public LiveData<EventType> createEventType(EventTypeRequestDto eventTypeDto) {
        return eventTypeRepository.createEventType(eventTypeDto);
    }
}
