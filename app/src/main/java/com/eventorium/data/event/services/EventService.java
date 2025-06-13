package com.eventorium.data.event.services;

import com.eventorium.data.event.models.EventRatingsStatistics;
import com.eventorium.data.event.models.PastEvent;
import com.eventorium.data.event.models.event.Activity;
import com.eventorium.data.event.models.event.CalendarEvent;
import com.eventorium.data.event.models.event.CreateEvent;
import com.eventorium.data.event.models.event.EditableEvent;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventDetails;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.models.event.UpdateEvent;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EventService {

    @GET("events/{id}/details")
    Call<EventDetails> getEventDetails(@Path("id") Long id);

    @GET("events/all")
    Call<List<EventSummary>> getAll();

    @GET("events/top-five-events")
    Call<List<EventSummary>> getTopEvents();

    @GET("events/future")
    Call<List<Event>> getFutureEvents();

    @POST("events")
    Call<Event> createEvent(@Body CreateEvent event);

    @PUT("events/{id}/agenda")
    Call<ResponseBody> createAgenda(@Path("id") Long id, @Body List<Activity> agenda);

    @GET("events/search/all")
    Call<List<EventSummary>> searchEvents(@Query("keyword") String keyword);

    @GET("account/events/my-attending-events")
    Call<List<CalendarEvent>> getAttendingEvents();

    @GET("account/events/calendar")
    Call<List<CalendarEvent>> getOrganizerEvents();

    @GET("events/{id}/pdf")
    Call<ResponseBody> exportToPdf(@Path("id") Long id);

    @GET("events/{id}/guest-list-pdf")
    Call<ResponseBody> exportGuestListToPdf(@Path("id") Long id);

    @GET("events/{id}/agenda")
    Call<List<Activity>> getAgenda(@Path("id") Long id);

    @GET("events/filter/all")
    Call<List<EventSummary>> filterEvents(@QueryMap Map<String, String> params);
  
    @GET("events/{id}")
    Call<EditableEvent> getEditableEvent(@Path("id") Long id);

    @PUT("events/{id}")
    Call<ResponseBody> updateEvent(@Path("id") Long id, @Body UpdateEvent event);

    @GET("events/passed")
    Call<List<PastEvent>> getPassedEvents();

    @GET("events/{id}/pdf-statistics")
    Call<ResponseBody> exportStatisticsToPdf(@Path("id") Long id);

    @GET("events/{id}/statistics")
    Call<EventRatingsStatistics> getStatistics(@Path("id") Long id);
}