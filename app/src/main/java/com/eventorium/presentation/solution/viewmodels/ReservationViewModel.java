package com.eventorium.presentation.solution.viewmodels;

import static java.util.stream.Collectors.toList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.models.Status;
import com.eventorium.data.solution.models.service.Reservation;
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
    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

    @Inject
    public ReservationViewModel(ReservationRepository repository) {
        this.repository = repository;
    }

    public void observePendingReservations() {
        repository.getPendingReservations().observeForever(this.reservations::postValue);
    }

    public LiveData<List<Reservation>> getPendingReservations() {
        return reservations;
    }

    public void removeReservation(Long id) {
        reservations.setValue(Objects.requireNonNull(reservations.getValue()).stream()
                .filter(reservation -> !reservation.getId().equals(id))
                .collect(toList()));
    }

    public LiveData<Result<Reservation>> updateReservation(Long id, Status status) {
        return repository.updateReservation(id, new UpdateReservationStatus(status));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.getPendingReservations().removeObserver(reservations::postValue);
    }
}
