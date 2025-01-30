package com.eventorium.data.event.services;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.solution.models.service.CalendarReservation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {

    @GET("events/{id}")
    Call<EventDetails> getEventDetails(@Path("id") Long id);

    @GET("events/all")
    Call<List<EventSummary>> getAll();

    @GET("events/top-five-events")
    Call<List<EventSummary>> getTopEvents();

    @POST("events")
    Call<Event> createEvent(@Body CreateEvent event);

    @PUT("events/{id}/agenda")
    Call<Void> createAgenda(@Path("id") Long id, @Body List<Activity> agenda);

    @GET("events/search/all")
    Call<List<EventSummary>> searchEvents(@Query("keyword") String keyword);

    @GET("account/events/my-attending-events")
    Call<List<CalendarEvent>> getAttendingEvents();

    @GET("account/events/my-events")
    Call<List<CalendarEvent>> getOrganizerEvents();

    @GET("provider-reservations")
    Call<List<CalendarReservation>> getReservations();
}