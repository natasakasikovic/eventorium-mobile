package com.eventorium.data.event.repositories;

import static java.util.stream.Collectors.toList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.dtos.EventTypeRequestDto;
import com.eventorium.data.event.dtos.EventTypeResponseDto;
import com.eventorium.data.event.mappers.EventTypeMapper;
import com.eventorium.data.event.models.EventType;
import com.eventorium.data.event.services.EventTypeService;

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

    public LiveData<EventType> createEventType(EventTypeRequestDto dto) {
        MutableLiveData<EventType> liveData = new MutableLiveData<>();

        eventTypeService.createEventType(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<EventTypeResponseDto> call,
                    @NonNull Response<EventTypeResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(EventTypeMapper.fromResponse(response.body()));
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<EventTypeResponseDto> call,
                    @NonNull Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });

        return liveData;
    }

    public LiveData<List<EventType>> getEventTypes() {
        MutableLiveData<List<EventType>> liveData = new MutableLiveData<>();

        eventTypeService.getEventTypes().enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<EventTypeResponseDto>> call,
                    @NonNull Response<List<EventTypeResponseDto>> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body().stream()
                            .map(EventTypeMapper::fromResponse)
                            .collect(toList()));
                } else {
                    Log.e("API_ERROR", "Error: " + response.code() + " - " + response.message());
                    liveData.postValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<EventTypeResponseDto>> call,
                    @NonNull Throwable t
            ) {
                Log.e("API_ERROR", "Error: " + t.getLocalizedMessage());
                liveData.postValue(new ArrayList<>());
            }
        });

        return liveData;
    }
}
