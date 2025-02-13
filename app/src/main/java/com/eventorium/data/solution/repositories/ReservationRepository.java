package com.eventorium.data.solution.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.data.solution.models.service.UpdateReservation;
import com.eventorium.data.solution.services.ReservationService;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRepository {

    private final ReservationService service;

    @Inject
    public ReservationRepository(ReservationService service) {
        this.service = service;
    }

    public LiveData<Result<List<Reservation>>> getPendingReservations() {
        MutableLiveData<Result<List<Reservation>>> result = new MutableLiveData<>();
        service.getPendingReservations().enqueue(handleRequest(result));
        return result;
    }

    public LiveData<Result<Reservation>> updateReservation(Long id, UpdateReservation request) {
        MutableLiveData<Result<Reservation>> result = new MutableLiveData<>();
        service.updateReservation(id, request).enqueue(handleRequest(result));
        return result;
    }


    private<T> Callback<T> handleRequest(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

}
