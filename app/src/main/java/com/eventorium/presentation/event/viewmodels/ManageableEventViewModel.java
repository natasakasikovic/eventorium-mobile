package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableEventViewModel extends ViewModel {

    private final AccountEventRepository repository;

    @Inject
    public ManageableEventViewModel(AccountEventRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<EventSummary>>> getEvents() {
        return repository.getManageableEvents();
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        return repository.searchEvents(keyword);
    }
}
