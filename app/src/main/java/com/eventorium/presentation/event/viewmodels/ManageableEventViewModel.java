package com.eventorium.presentation.event.viewmodels;

import androidx.lifecycle.LiveData;

import com.eventorium.data.event.models.event.EventFilter;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.repositories.AccountEventRepository;
import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.presentation.shared.models.PagingMode;
import com.eventorium.presentation.shared.viewmodels.PagedViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManageableEventViewModel extends PagedViewModel<EventSummary, EventFilter> {

    private final AccountEventRepository repository;

    @Inject
    public ManageableEventViewModel(AccountEventRepository repository) {
        this.repository = repository;
    }
    
    @Override
    protected LiveData<Result<PagedResponse<EventSummary>>> loadPage(PagingMode mode, int page, int size) {
        return switch (mode) {
            case DEFAULT -> repository.getManageableEvents(page, size);
            case SEARCH -> repository.searchEvents(searchQuery, page, size);
            case FILTER -> throw new UnsupportedOperationException("Manageable events filter not supported");
            case SORT -> throw new UnsupportedOperationException("Manageable events sort not supported");
        };
    }
}
