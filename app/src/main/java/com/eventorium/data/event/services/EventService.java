package com.eventorium.data.event.services;

import com.eventorium.data.event.models.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventService {

    @GET("events/all")
    Call<List<Event>> getAll();
}