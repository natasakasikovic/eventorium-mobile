package com.eventorium.data.shared.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.shared.models.City;
import com.eventorium.data.shared.services.CityService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityRepository {
    private final CityService service;

    @Inject
    public CityRepository(CityService service){
        this.service = service;
    }

    public LiveData<List<City>> getCities(){

        MutableLiveData<List<City>> liveData = new MutableLiveData<>();

        service.getAll().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                liveData.postValue(null);
            }
        });
        return liveData;
    }
}
