package com.eventorium.data.event.services;

import com.eventorium.data.event.models.EventSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventService {

    @GET("events/all")
    Call<List<EventSummary>> getAll();

    @GET("events/top-five-events")
    Call<List<EventSummary>> getTopEvents();
}