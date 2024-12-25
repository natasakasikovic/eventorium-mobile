package com.eventorium.presentation.homepage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.EventRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomepageViewModel extends ViewModel {
    private final EventRepository eventRepository;

    @Inject
    public HomepageViewModel(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }
    public LiveData<Result<List<EventSummary>>> getTopEvents(){
        return eventRepository.getTopEvents();
    }
}
