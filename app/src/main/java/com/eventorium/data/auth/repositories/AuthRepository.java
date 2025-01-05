package com.eventorium.data.auth.repositories;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.JWT;
import com.eventorium.data.auth.dtos.LoginRequestDto;
import com.eventorium.data.auth.dtos.LoginResponseDto;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.services.WebSocketService;
import com.eventorium.presentation.util.JwtDecoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthService authService;
    private final SharedPreferences sharedPreferences;

    private final WebSocketService webSocketService;

    public AuthRepository(WebSocketService webSocketService, AuthService authService, SharedPreferences sharedPreferences) {
        this.webSocketService = webSocketService;
        this.authService = authService;
        this.sharedPreferences = sharedPreferences;
    }

    public LiveData<Result<LoginResponseDto>> login(LoginRequestDto dto) {
        MutableLiveData<Result<LoginResponseDto>> liveData = new MutableLiveData<>();
        authService.login(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulResponse(response.body(), liveData);
                } else {
                    handleErrorResponse(response, liveData);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                liveData.postValue(Result.error("An error occurred. Please try again later."));
            }
        });
        return liveData;
    }

    private void handleSuccessfulResponse(LoginResponseDto responseBody, MutableLiveData<Result<LoginResponseDto>> liveData) {
        saveJwtToken(responseBody.getJwt());
        liveData.postValue(Result.success(responseBody));
    }

    private void handleErrorResponse(Response<LoginResponseDto> response, MutableLiveData<Result<LoginResponseDto>> liveData) {
        String errorMessage = getErrorMessage(response.code());
        liveData.postValue(Result.error(errorMessage));
    }

    private String getErrorMessage(int errorCode) {
        return switch (errorCode) {
            case 401 -> "Invalid credentials. Please try again.";
            case 403 -> "Account is not verified. Check your email.";
            default -> "An error occurred. Please try again later.";
        };
    }

    private void saveJwtToken(String jwtToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", jwtToken);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getString("user", null) != null;
    }

    public Long getUserId() {
        if(!isLoggedIn()) {
            return null;
        }
        return new JWT(sharedPreferences.getString("user", ""))
                .getClaim("userId").asLong();
    }
    public String saveRole(String jwt) {
        String role = JwtDecoder.decodeRole(jwt);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("role", role);
        editor.apply();
        return role;
    }
}
