package com.eventorium.data.auth.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.Role;
import com.eventorium.data.auth.services.RoleService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoleRepository {

    private final RoleService service;

    @Inject
    public RoleRepository(RoleService service) {
        this.service = service;
    }

    public LiveData<List<Role>> getRegistrationRoles() {
        MutableLiveData<List<Role>> liveData = new MutableLiveData<>();

        service.getRegistrationRoles().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }
}
