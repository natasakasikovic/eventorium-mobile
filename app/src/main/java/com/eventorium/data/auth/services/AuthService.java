package com.eventorium.data.auth.services;


import com.eventorium.data.auth.dtos.LoginRequestDto;
import com.eventorium.data.auth.dtos.LoginResponseDto;
import com.eventorium.data.auth.models.User;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthService {

    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequestDto);

    @POST("auth/registration")
    Call<User> createAccount(@Body User user);

    @Multipart
    @POST("auth/{userId}/profile-photo")
    Call<ResponseBody> uploadProfilePhoto(@Path("userId") Long userId, @Part MultipartBody.Part profilePhoto);
}
