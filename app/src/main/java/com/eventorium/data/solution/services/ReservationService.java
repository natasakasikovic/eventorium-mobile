package com.eventorium.data.solution.services;

import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.data.solution.models.service.UpdateReservationStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ReservationService {

    @GET("reservations/pending")
    Call<List<Reservation>> getPendingReservations();

    @PATCH("reservations/{id}")
    Call<Reservation> updateReservation(@Path("id") Long id, @Body UpdateReservationStatus request);
}
