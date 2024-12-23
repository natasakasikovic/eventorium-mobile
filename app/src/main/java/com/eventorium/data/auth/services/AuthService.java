package com.eventorium.data.auth.services;


import com.eventorium.data.auth.dtos.LoginRequestDto;
import com.eventorium.data.auth.dtos.LoginResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequestDto);
}
