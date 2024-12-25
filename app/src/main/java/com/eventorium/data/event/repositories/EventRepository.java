package com.eventorium.data.event.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.Event;
import com.eventorium.data.event.services.EventService;
import com.eventorium.data.util.Result;

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

    public LiveData<Result<List<Event>>> getEvents(){

        MutableLiveData<Result<List<Event>>> liveData = new MutableLiveData<>();

        service.getAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(Result.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                liveData.postValue(Result.error("Oops! Something went wrong! Please, try again later!"));
            }
        });
        return liveData;
    }

}
