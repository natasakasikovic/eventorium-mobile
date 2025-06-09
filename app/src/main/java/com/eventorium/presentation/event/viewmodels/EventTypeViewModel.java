package com.eventorium.presentation.event.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.repositories.EventTypeRepository;
import com.eventorium.data.shared.models.Result;

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

    public LiveData<List<EventType>> getEventTypes() {
        return eventTypeRepository.getEventTypes();
    }

    public LiveData<Bitmap> getImage(Long id) {
        return eventTypeRepository.getImage(id);
    }

    public LiveData<Result<Void>> update(EventType eventType) {
        return eventTypeRepository.updateEventType(eventType);
    }

    public LiveData<Result<Void>> delete(Long id) {
        return eventTypeRepository.deleteEventType(id);
    }

    public LiveData<Boolean> uploadImage(Long id, Context context, Uri selectedImageUri) {
        return eventTypeRepository.uploadImage(id, context, selectedImageUri);
    }

    public LiveData<Boolean> updateImage(Long id, Context context, Uri selectedImageUri) {
        return eventTypeRepository.updateImage(id, context, selectedImageUri);
    }
}
