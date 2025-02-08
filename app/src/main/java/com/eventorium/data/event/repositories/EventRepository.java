package com.eventorium.data.event.repositories;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Activity;
import com.eventorium.data.event.models.CalendarEvent;
import com.eventorium.data.event.models.CreateEvent;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.models.EventDetails;
import com.eventorium.data.event.models.EventSummary;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.List;

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

    public LiveData<Result<List<EventSummary>>> getEvents(){

        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();

        service.getAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<EventSummary>> call, Response<List<EventSummary>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<EventSummary>> call, Throwable t) {
                liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });
        return liveData;
    }

    public LiveData<Result<List<EventSummary>>> getTopEvents(){

        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();

        service.getTopEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<EventSummary>> call, Response<List<EventSummary>> response) {
                if (response.body() != null && response.isSuccessful()){
                    liveData.postValue(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<EventSummary>> call, Throwable t) {
                liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                }
            }
        );
        return liveData;
    }

    public LiveData<Result<Event>> createEvent(CreateEvent event) {
        MutableLiveData<Result<Event>> liveData = new MutableLiveData<>();
        service.createEvent(event).enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
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
            public void onFailure(Call<Event> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Result<Void>> createAgenda(Long id, List<Activity> agenda) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.createAgenda(id, agenda).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) result.postValue(Result.success(null));
                else result.postValue(Result.error(ErrorMessages.INVALID_ACTIVITY));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });
        return result;
    }

    public LiveData<Result<List<EventSummary>>> searchEvents(String keyword) {
        MutableLiveData<Result<List<EventSummary>>> liveData = new MutableLiveData<>();

        service.searchEvents(keyword).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<EventSummary>> call, @NonNull Response<List<EventSummary>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EventSummary>> call, @NonNull Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    public LiveData<Result<EventDetails>> getEventDetails(Long id) {
        MutableLiveData<Result<EventDetails>> result = new MutableLiveData<>();

        service.getEventDetails(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<EventDetails> call, Response<EventDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading event"));
                }
            }

            @Override
            public void onFailure(Call<EventDetails> call, Throwable t) {
                result.postValue(Result.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Result<List<CalendarEvent>>> getAttendingEvents() {
        MutableLiveData<Result<List<CalendarEvent>>> result = new MutableLiveData<>();

        service.getAttendingEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, Response<List<CalendarEvent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading events."));
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                result.postValue(Result.error("Error while loading events."));
            }
        });

        return result;
    }

    public LiveData<Result<List<CalendarEvent>>> getOrganizerEvents() {
        MutableLiveData<Result<List<CalendarEvent>>> result = new MutableLiveData<>();

        service.getOrganizerEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, Response<List<CalendarEvent>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading events organized by you."));
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                result.postValue(Result.error("Error while loading events organized by you."));
            }
        });

        return result;
    }

    public LiveData<Result<Uri>> exportToPdf(Long id, Context context) {
        MutableLiveData<Result<Uri>> result = new MutableLiveData<>();
        service.exportToPdf(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Uri uri = FileUtil.savePdfToDownloads(context, response.body());
                        if (uri != null) result.postValue(Result.success(uri));
                        else result.postValue(Result.error("Failed to export pdf."));
                    } catch (IOException e) {
                        result.postValue(Result.error("Failed to export pdf."));
                    }
                } else {
                    result.postValue(Result.error("Failed to export pdf."));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(Result.error("Failed to export pdf."));
            }
        });

        return result;
    }

    public LiveData<Result<List<Activity>>> getAgenda(Long id) {
        MutableLiveData<Result<List<Activity>>> result = new MutableLiveData<>();

        service.getAgenda(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error("Error while loading event agenda"));
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                result.postValue(Result.error("Error while loading event agenda"));
            }
        });

        return result;
    }
}
