package com.eventorium.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.dtos.eventtypes.EventTypeRequestDto;
import com.eventorium.data.dtos.eventtypes.EventTypeResponseDto;
import com.eventorium.data.mappers.EventTypeMapper;
import com.eventorium.data.models.EventType;
import com.eventorium.data.services.EventTypeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeRepository {

    private final EventTypeService eventTypeService;

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
}
