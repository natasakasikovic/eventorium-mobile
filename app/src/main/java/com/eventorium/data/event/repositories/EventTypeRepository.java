package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.services.EventTypeService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeRepository {

    private final EventTypeService eventTypeService;

    @Inject
    public EventTypeRepository(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    public LiveData<EventType> createEventType(CreateEventType eventType) {
        MutableLiveData<EventType> liveData = new MutableLiveData<>();
        eventTypeService.createEventType(eventType).enqueue(handleSuccessfulResponse(liveData));
        return liveData;
    }

    public LiveData<List<EventType>> getEventTypes() {
        MutableLiveData<List<EventType>> liveData = new MutableLiveData<>();
        eventTypeService.getEventTypes().enqueue(handleSuccessfulResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> updateEventType(EventType eventType) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        eventTypeService.updateEventType(eventType.getId(), eventType).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> deleteEventType(Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        eventTypeService.delete(id).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }
}
