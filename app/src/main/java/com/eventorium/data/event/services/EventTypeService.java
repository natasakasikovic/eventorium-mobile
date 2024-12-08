package com.eventorium.data.event.services;

import com.eventorium.data.event.dtos.EventTypeRequestDto;
import com.eventorium.data.event.dtos.EventTypeResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventTypeService {
    @GET("event-types/all")
    Call<List<EventTypeResponseDto>> getEventTypes();

    @POST("event-types")
    Call<EventTypeResponseDto> createEventType(@Body EventTypeRequestDto dto);
}
