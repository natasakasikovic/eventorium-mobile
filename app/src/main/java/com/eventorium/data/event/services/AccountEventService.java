package com.eventorium.data.event.services;

import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.shared.models.PagedResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountEventService {

    @GET("account/events/favourites/{id}")
    Call<Boolean> isFavouriteEvent(@Path("id") Long id);

    @POST("account/events/favourites/{id}")
    Call<ResponseBody> addToFavourites(@Path("id") Long id);

    @DELETE("account/events/favourites/{id}")
    Call<ResponseBody> removeFromFavourites(@Path("id") Long id);

    @GET("account/events/favourites")
    Call<List<EventSummary>> getFavouriteEvents();

    @GET("account/events/{id}/rating-eligibility")
    Call<Boolean> isUserEligibleToRate(@Path("id") Long id);

    @GET("account/events")
    Call<PagedResponse<EventSummary>> getManageableEvents(@Query("page") int page, @Query("size") int size);

    @GET("account/events/search/all")
    Call<List<EventSummary>> searchEvents(@Query("keyword") String keyword);

    @POST("account/events/{id}/attendance")
    Call<ResponseBody> addToCalendar(@Path("id") Long id);
}
