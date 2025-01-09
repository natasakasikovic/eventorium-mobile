package com.eventorium.data.auth.services;

import com.eventorium.data.auth.models.AccountDetails;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {
    @GET("users/me")
    Call<AccountDetails> getCurrentUser();

    @GET("users/{id}/profile-photo")
    Call<ResponseBody> getProfilePhoto(@Path("id") Long id);
}