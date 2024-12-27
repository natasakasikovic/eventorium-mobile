package com.eventorium.data.shared.services;


import com.eventorium.data.shared.models.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CityService {

    @GET("cities/all")
    Call<List<City>> getAll();
}
