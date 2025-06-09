package com.eventorium.data.solution.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;
import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.data.solution.models.service.UpdateReservationStatus;
import com.eventorium.data.solution.services.ReservationService;

import java.util.List;

import javax.inject.Inject;

public class ReservationRepository {

    private final ReservationService service;

    @Inject
    public ReservationRepository(ReservationService service) {
        this.service = service;
    }

    public LiveData<List<Reservation>> getPendingReservations() {
        MutableLiveData<List<Reservation>> result = new MutableLiveData<>();
        service.getPendingReservations().enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Result<Reservation>> updateReservation(Long id, UpdateReservationStatus request) {
        MutableLiveData<Result<Reservation>> result = new MutableLiveData<>();
        service.updateReservation(id, request).enqueue(RetrofitCallbackHelper.handleGeneralResponse(result));
        return result;
    }

}
