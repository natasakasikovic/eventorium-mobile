package com.eventorium.presentation.event.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EventViewModel extends ViewModel {

    private final EventRepository repository;
    private final AccountEventRepository accountEventRepository;

    @Inject
    public EventViewModel(EventRepository eventRepository, AccountEventRepository accountEventRepository) {
        this.repository = eventRepository;
        this.accountEventRepository = accountEventRepository;
    }

    public LiveData<Result<List<EventSummary>>> getEvents(){
        return repository.getEvents();
    }

    public LiveData<Result<Event>> createEvent(CreateEvent event) {
        return repository.createEvent(event);
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        return repository.searchEvents(keyword);
    }

    public LiveData<Result<EventDetails>> getEventDetails(Long id) {
        return repository.getEventDetails(id);
    }

    public LiveData<Result<Void>> createAgenda(Long id, List<Activity> agenda) {
        return repository.createAgenda(id, agenda);
    }

    public LiveData<Boolean> isFavourite(Long id) {
        return accountEventRepository.isFavouriteEvent(id);
    }

    public LiveData<Result<Void>> addToFavourites(Long id) {
        return accountEventRepository.addToFavourites(id);
    }

    public LiveData<Result<Void>> removeFromFavourites(Long id) {
        return accountEventRepository.removeFromFavourites(id);
    }

    public LiveData<Result<Void>> addToCalendar(Long id) {
        return accountEventRepository.addToCalendar(id);
    }

    public LiveData<Result<Uri>> exportToPdf(Long id, Context context) {
        return repository.exportToPdf(id, context);
    }

    public LiveData<Result<List<Activity>>> getAgenda(Long id) {
        return repository.getAgenda(id);
    }
}