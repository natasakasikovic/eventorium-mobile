package com.eventorium.data.auth.services;

import com.eventorium.data.auth.models.Role;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RoleService {

    @GET("roles/registration-options")
    Call<List<Role>> getRegistrationRoles();
}
