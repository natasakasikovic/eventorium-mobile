package com.eventorium.data.event.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.EditableEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.models.UpdateEvent;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;

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
        MutableLiveData<Result<List<Event>>> result = new MutableLiveData<>();
        service.getDraftedEvents().enqueue(handleValidationResponse(result));
        return result;
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
}
