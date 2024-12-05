package com.eventorium.data.services;

import com.eventorium.data.dtos.eventtypes.EventTypeRequestDto;
import com.eventorium.data.dtos.eventtypes.EventTypeResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EventTypeService {

    @POST("event-types")
    Call<EventTypeResponseDto> createEventType(@Body EventTypeRequestDto dto);
}
