package com.eventorium.data.event.services;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventTypeService {
    @GET("event-types/all")
    Call<List<EventType>> getEventTypes();

    @POST("event-types")
    Call<EventType> createEventType(@Body CreateEventType dto);

    @PUT("event-types/{id}")
    Call<Void> updateEventType(@Path("id") Long id, @Body EventType eventType);
}
