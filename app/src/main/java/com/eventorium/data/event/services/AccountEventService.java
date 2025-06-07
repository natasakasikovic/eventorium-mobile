package com.eventorium.data.event.services;

import com.eventorium.data.event.models.EventSummary;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountEventService {

    @GET("account/events/favourites/{id}")
    Call<Boolean> isFavouriteEvent(@Path("id") Long id);

    @POST("account/events/favourites/{id}")
    Call<ResponseBody> addToFavourites(@Path("id") Long id);

    @DELETE("account/events/favourites/{id}")
    Call<ResponseBody> removeFromFavourites(@Path("id") Long id);

    @GET("account/events/favourites")
    Call<List<EventSummary>> getFavouriteEvents();

    @GET("account/events/my-events")
    Call<List<EventSummary>> getOrganizerEvents();

    @POST("account/events/{id}/attendance")
    Call<ResponseBody> addToCalendar(@Path("id") Long id);
}
