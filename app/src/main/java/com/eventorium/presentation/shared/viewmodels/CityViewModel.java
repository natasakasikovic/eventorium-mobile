package com.eventorium.presentation.shared.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.shared.models.City;
import com.eventorium.data.shared.repositories.CityRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CityViewModel extends ViewModel {
    private final CityRepository repository;

    @Inject
    public CityViewModel(CityRepository cityRepository) {
        this.repository = cityRepository;
    }

    public LiveData<List<City>> getCities(){
        return repository.getCities();
    }
}
