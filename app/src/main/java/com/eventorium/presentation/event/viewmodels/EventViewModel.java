package com.eventorium.presentation.event.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventRatingsStatistics;
import com.eventorium.data.event.models.PastEvent;
import com.eventorium.data.event.models.event.Activity;
import com.eventorium.data.event.models.event.CreateEvent;
import com.eventorium.data.event.models.event.EditableEvent;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventDetails;
import com.eventorium.data.event.models.event.EventFilter;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.models.event.UpdateEvent;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.ResponseBody;

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

    public LiveData<Result<List<Event>>> getFutureEvents() {
        return repository.getFutureEvents();
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

    public LiveData<Result<Boolean>> isUserEligibleToRate(Long eventId) {
        return accountEventRepository.isUserEligibleToRate(eventId);
    }

    public LiveData<Result<Void>> addToCalendar(Long id) {
        return accountEventRepository.addToCalendar(id);
    }

    public LiveData<Result<Uri>> exportToPdf(Long id, Context context) {
        return repository.exportToPdf(id, context);
    }

    public LiveData<Result<Uri>> exportGuestListToPdf(Long id, Context context) {
        return repository.exportGuestListToPdf(id, context);
    }

    public LiveData<Result<Uri>> exportEventStatisticsToPdf(Long id, Context context) {
        return repository.exportEventStatisticsToPdf(id, context);
    }

    public LiveData<Result<List<Activity>>> getAgenda(Long id) {
        return repository.getAgenda(id);
    }

    public LiveData<Result<List<EventSummary>>> filterEvents(EventFilter filter) {
        return repository.filterEvents(filter);
    }
  
    public LiveData<Result<EditableEvent>> getEditableEvent(Long id) {
        return repository.getEditableEvent(id);
    }

    public LiveData<Result<ResponseBody>> updateEvent(Long eventId, UpdateEvent event) {
        return repository.updateEvent(eventId, event);
    }

    public LiveData<Result<List<PastEvent>>> getPassedEvents() {
        return repository.getPassedEvents();
    }

    public LiveData<Result<EventRatingsStatistics>> getStatistics(Long id) {
        return repository.getStatistics(id);
    }
}