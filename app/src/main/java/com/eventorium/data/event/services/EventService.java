package com.eventorium.data.event.services;

import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventService {

    @GET("events/all")
    Call<List<EventSummary>> getAll();

    @GET("events/top-five-events")
    Call<List<EventSummary>> getTopEvents();

    @GET("events/drafted")
    Call<List<Event>> getDraftedEvents();

    @POST("events")
    Call<Event> createEvent(@Body CreateEvent event);
}