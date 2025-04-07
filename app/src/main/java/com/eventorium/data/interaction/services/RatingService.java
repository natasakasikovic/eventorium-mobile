package com.eventorium.data.interaction.services;

import com.eventorium.data.interaction.models.rating.CreateRating;
import com.eventorium.data.interaction.models.rating.Rating;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RatingService {

    @POST("services/{service-id}/ratings")
    Call<Rating> createServiceRating(@Path("service-id") Long id, @Body CreateRating request);

    @POST("products/{product-id}/ratings")
    Call<Rating> createProductRating(@Path("product-id") Long id, @Body CreateRating request);

    @POST("events/{event-id}/ratings")
    Call<Rating> createEventRating(@Path("event-id") Long id, @Body CreateRating request);

}
