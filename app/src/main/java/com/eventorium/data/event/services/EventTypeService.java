package com.eventorium.data.event.services;

import com.eventorium.data.event.models.CreateEventType;
import com.eventorium.data.event.models.EventType;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EventTypeService {
    @GET("event-types/all")
    Call<List<EventType>> getEventTypes();

    @GET("event-types/{id}/image")
    Call<ResponseBody> getImage(@Path("id") Long id);

    @POST("event-types")
    Call<EventType> createEventType(@Body CreateEventType dto);

    @Multipart
    @POST("event-types/{id}/image")
    Call<ResponseBody> uploadImage(@Path("id") Long id, @Part MultipartBody.Part image);

    @PUT("event-types/{id}")
    Call<Void> updateEventType(@Path("id") Long id, @Body EventType eventType);

    @Multipart
    @PUT("event-types/{id}")
    Call<ResponseBody> updateImage(@Path("id") Long id, @Part MultipartBody.Part image);

    @DELETE("event-types/{id}")
    Call<Void> delete(@Path("id") Long id);
}
