package com.eventorium.data.auth.services;

import com.eventorium.data.auth.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("auth/registration")
    Call<User> createAccount(@Body User user);
}
