package com.eventorium.data.event.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.services.EventService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {

    private final EventService service;

    @Inject
    public EventRepository(EventService service){
        this.service = service;
    }

    public LiveData<List<Event>> getEvents(){

        MutableLiveData<List<Event>> liveData = new MutableLiveData<>();

        service.getAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage()); // TODO: replace with e.g. toast
            }
        });
        return liveData;
    }

}
