package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleSuccessfulResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.services.AccountEventService;
import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;

import java.util.List;

public class AccountEventRepository {

    private final AccountEventService service;

    public AccountEventRepository(AccountEventService service) {
        this.service = service;
    }

    public LiveData<Result<PagedResponse<EventSummary>>> getManageableEvents(int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> result = new MutableLiveData<>();
        service.getManageableEvents(page, size).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<EventSummary>>> searchEvents(String keyword, int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> result = new MutableLiveData<>();
        service.searchEvents(keyword, page, size).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Boolean> isFavouriteEvent(Long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        service.isFavouriteEvent(id).enqueue(handleSuccessfulResponse(result));
        return result;
    }

    public LiveData<Result<Void>> addToFavourites(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addToFavourites(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<Void>> removeFromFavourites(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.removeFromFavourites(id).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<List<EventSummary>>> getFavouriteEvents() {
        MutableLiveData<Result<List<EventSummary>>> result = new MutableLiveData<>();
        service.getFavouriteEvents().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Boolean>> isUserEligibleToRate(Long eventId) {
        MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        service.isUserEligibleToRate(eventId).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> addToCalendar(Long id) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.addToCalendar(id).enqueue(handleVoidResponse(result));
        return result;
    }
}
