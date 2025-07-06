package com.eventorium.data.auth.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.Role;
import com.eventorium.data.auth.services.RoleService;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

import java.util.List;

import javax.inject.Inject;

public class RoleRepository {

    private final RoleService service;

    @Inject
    public RoleRepository(RoleService service) {
        this.service = service;
    }

    public LiveData<List<Role>> getRegistrationRoles() {
        MutableLiveData<List<Role>> liveData = new MutableLiveData<>();
        service.getRegistrationRoles().enqueue(RetrofitCallbackHelper.handleSuccessfulResponse(liveData));
        return liveData;
    }
}
