package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handlePdfExport;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleValidationResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.EventRatingsStatistics;
import com.eventorium.data.event.models.PastEvent;
import com.eventorium.data.event.models.event.Activity;
import com.eventorium.data.event.models.event.CalendarEvent;
import com.eventorium.data.event.models.event.CreateEvent;
import com.eventorium.data.event.models.event.EditableEvent;
import com.eventorium.data.event.models.event.Event;
import com.eventorium.data.event.models.event.EventDetails;
import com.eventorium.data.event.models.event.EventFilter;
import com.eventorium.data.event.models.event.EventSummary;
import com.eventorium.data.event.models.event.UpdateEvent;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.shared.models.PagedResponse;
import com.eventorium.data.shared.models.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class EventRepository {

    private final EventService service;

    @Inject
    public EventRepository(EventService service){
        this.service = service;
    }

    public LiveData<Result<PagedResponse<EventSummary>>> getEvents(int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> liveData = new MutableLiveData<>();
        service.getEvents(page, size).enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<PagedResponse<EventSummary>>> getSortedEvents(String sortCriteria, int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> liveData = new MutableLiveData<>();
        service.getSortedEvents(sortCriteria, page, size).enqueue(handleGeneralResponse(liveData));
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

    public LiveData<Result<List<Event>>> getFutureEvents() {
        MutableLiveData<Result<List<Event>>> result = new MutableLiveData<>();
        service.getFutureEvents().enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<Void>> createAgenda(Long id, List<Activity> agenda) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.createAgenda(id, agenda).enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<PagedResponse<EventSummary>>> searchEvents(String keyword, int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> liveData = new MutableLiveData<>();
        service.searchEvents(keyword, page, size).enqueue(handleGeneralResponse(liveData));
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

    public LiveData<Result<Uri>> exportEventStatisticsToPdf(Long id, Context context) {
        return executeExport(service.exportStatisticsToPdf(id), context);
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

    public LiveData<Result<PagedResponse<EventSummary>>> filterEvents(EventFilter filter, int page, int size) {
        MutableLiveData<Result<PagedResponse<EventSummary>>> result = new MutableLiveData<>();
        service.filterEvents(getFilterParams(filter, page, size)).enqueue(handleGeneralResponse(result));
        return result;
    }

    private Map<String, String> getFilterParams(EventFilter filter, int page, int size) {
        Map<String, String> params = new HashMap<>();

        addParamIfNotNull(params, "name", filter.getName());
        addParamIfNotNull(params, "description", filter.getDescription());
        addParamIfNotNull(params, "type", filter.getType());
        addParamIfNotNull(params, "maxParticipants", filter.getMaxParticipants());
        addParamIfNotNull(params, "city", filter.getCity());
        addParamIfNotNull(params, "from", filter.getFrom());
        addParamIfNotNull(params, "to", filter.getTo());
        addParamIfNotNull(params, "page", page);
        addParamIfNotNull(params, "size", size);

        return params;
    }

    private void addParamIfNotNull(Map<String, String> params, String key, Object value) {
        Optional.ofNullable(value)
                .filter(v -> !(v instanceof Boolean && Boolean.FALSE.equals(v)))
                .filter(v -> !(v instanceof String && v.toString().isEmpty()))
                .ifPresent(v -> params.put(key, v.toString()));
    }
  
    public LiveData<Result<EditableEvent>> getEditableEvent(Long id) {
        MutableLiveData<Result<EditableEvent>> result = new MutableLiveData<>();
        service.getEditableEvent(id).enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<ResponseBody>> updateEvent(Long id, UpdateEvent event) {
        MutableLiveData<Result<ResponseBody>> result = new MutableLiveData<>();
        service.updateEvent(id, event).enqueue(handleValidationResponse(result));
        return result;
    }

    public LiveData<Result<List<PastEvent>>> getPassedEvents() {
        MutableLiveData<Result<List<PastEvent>>> result = new MutableLiveData<>();
        service.getPassedEvents().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<EventRatingsStatistics>> getStatistics(Long id) {
        MutableLiveData<Result<EventRatingsStatistics>> result = new MutableLiveData<>();
        service.getStatistics(id).enqueue(handleGeneralResponse(result));
        return result;
    }
}
