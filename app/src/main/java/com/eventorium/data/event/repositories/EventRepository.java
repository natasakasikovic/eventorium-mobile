package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.event.models.EventFilter;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {

    private final EventService service;

    @Inject
    public EventRepository(EventService service){
        this.service = service;
    }

    public LiveData<Result<List<EventSummary>>> getEvents() {
        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();
        service.getAll().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<EventSummary>>> getTopEvents(){
        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();
        service.getTopEvents().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Event>> createEvent(CreateEvent event) {
        MutableLiveData<Result<Event>> liveData = new MutableLiveData<>();
        service.createEvent(event).enqueue(handleValidationResponse(liveData));
        return liveData;
    }

    public LiveData<Result<List<Event>>> getDraftedEvents() {
        MutableLiveData<Result<List<Event>>> liveData = new MutableLiveData<>();
        service.getDraftedEvents().enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success((response.body())));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Result<Void>> createAgenda(Long id, List<Activity> agenda) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.createAgenda(id, agenda).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();
        service.searchEvents(keyword).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<EventDetails>> getEventDetails(Long id) {
        MutableLiveData<Result<EventDetails>> result = new MutableLiveData<>();
        service.getEventDetails(id).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<CalendarEvent>>> getAttendingEvents() {
        MutableLiveData<Result<List<CalendarEvent>>> result = new MutableLiveData<>();
        service.getAttendingEvents().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<List<CalendarEvent>>> getOrganizerEvents() {
        MutableLiveData<Result<List<CalendarEvent>>> result = new MutableLiveData<>();
        service.getOrganizerEvents().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Uri>> exportToPdf(Long id, Context context) {
        return executeExport(service.exportToPdf(id), context);
    }

    public LiveData<Result<Uri>> exportGuestListToPdf(Long id, Context context) {
        return executeExport(service.exportGuestListToPdf(id), context);
    }

    private LiveData<Result<Uri>> executeExport(Call<ResponseBody> call, Context context) {
        MutableLiveData<Result<Uri>> result = new MutableLiveData<>();
        call.enqueue(handlePdfExport(context, result));
        return result;
    }

    public LiveData<Result<List<Activity>>> getAgenda(Long id) {
        MutableLiveData<Result<List<Activity>>> result = new MutableLiveData<>();
        service.getAgenda(id).enqueue(handleGeneralResponse(result));
        return result;
    }


    public LiveData<Result<List<EventSummary>>> filterEvents(EventFilter filter){
        MutableLiveData<Result<List<EventSummary>>> result = new MutableLiveData<>();
        service.filterEvents(getFilterParams(filter)).enqueue(handleGeneralResponse(result));
        return result;
    }

    private Map<String, String> getFilterParams(EventFilter filter) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "maxParticipants", filter.getMaxParticipants());
        addParamIfNotNull(params, "city", filter.getCity());
        addParamIfNotNull(params, "from", filter.getFrom());
        addParamIfNotNull(params, "to", filter.getTo());

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value) {
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }
}
