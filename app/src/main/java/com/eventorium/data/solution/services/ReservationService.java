package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.data.solution.models.service.ReservationRequest;
import com.eventorium.data.solution.models.service.UpdateReservationStatus;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService {

    @GET("reservations/pending")
    Call<List<Reservation>> getPendingReservations();

    @PATCH("reservations/{id}")
    Call<Reservation> updateReservation(@Path("id") Long id, @Body UpdateReservationStatus request);

    @POST("events/{eventId}/services/{serviceId}/reservation")
    Call<ResponseBody> reserveService(@Body ReservationRequest request, @Path("eventId") Long eventId, @Path("serviceId") long serviceId);
}