package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.event.models.event.EventFilter;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableEventViewModel extends PagedViewModel<EventSummary, EventFilter> {

    private final AccountEventRepository repository;

    @Inject
    public ManageableEventViewModel(AccountEventRepository repository) {
        super(2);
        this.repository = repository;
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        return repository.searchEvents(keyword);
    }

    @Override
    protected LiveData<Result<PagedResponse<EventSummary>>> loadPage(PagingMode mode, int page, int size) {
        return repository.getManageableEvents(page, size);
    }
}
