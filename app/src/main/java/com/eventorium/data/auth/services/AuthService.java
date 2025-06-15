package com.eventorium.data.auth.services;


import com.eventorium.data.auth.models.LoginRequest;
import com.eventorium.data.auth.models.AuthResponse;
import com.eventorium.data.auth.models.UpgradeAccountRequest;
import com.eventorium.data.auth.models.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/registration")
    Call<User> createAccount(@Body User user);

    @PUT("auth/account-role")
    Call<AuthResponse> upgradeAccount(@Body UpgradeAccountRequest request);

    @Multipart
    @POST("auth/{userId}/profile-photo")
    Call<ResponseBody> uploadProfilePhoto(@Path("userId") Long userId, @Part MultipartBody.Part profilePhoto);
}
