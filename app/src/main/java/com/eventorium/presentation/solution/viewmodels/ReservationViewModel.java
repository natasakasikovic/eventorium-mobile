package com.eventorium.presentation.solution.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;
import com.eventorium.data.solution.models.service.Reservation;
import com.eventorium.data.solution.models.service.ReservationRequest;
import com.eventorium.data.solution.models.service.UpdateReservationStatus;
import com.eventorium.data.solution.repositories.ReservationRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@Getter
@HiltViewModel
public class ReservationViewModel extends ViewModel {

    private final ReservationRepository repository;

    @Inject
    public ReservationViewModel(ReservationRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Reservation>> getPendingReservations() {
        return repository.getPendingReservations();
    }

    public LiveData<Result<Reservation>> updateReservation(Long id, Status status) {
        return repository.updateReservation(id, new UpdateReservationStatus(status));
    }

    public LiveData<Result<Void>> reserveService(String startingTime, String endingTime, Double plannedAmount, Long eventId, Long serviceId) {
        ReservationRequest request = new ReservationRequest(startingTime, endingTime, plannedAmount);
        return repository.reserveService(request, eventId, serviceId);
    }
}
