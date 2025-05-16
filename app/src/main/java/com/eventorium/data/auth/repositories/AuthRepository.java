package com.eventorium.data.auth.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.JWT;
import com.eventorium.data.auth.models.LoginRequest;
import com.eventorium.data.auth.models.LoginResponse;
import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.services.WebSocketService;
import com.eventorium.presentation.util.JwtDecoder;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthService authService;
    private final SharedPreferences sharedPreferences;

    private final WebSocketService webSocketService;

    @Inject
    public AuthRepository(WebSocketService webSocketService, AuthService authService, SharedPreferences sharedPreferences) {
        this.webSocketService = webSocketService;
        this.authService = authService;
        this.sharedPreferences = sharedPreferences;
    }

    public LiveData<Result<User>> createAccount(User user) {
        MutableLiveData<Result<User>> liveData = new MutableLiveData<>();
        authService.createAccount(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Boolean> uploadPhoto(Long id, Context context, Uri uri) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        MultipartBody.Part part;

        try {
            part = FileUtil.getImageFromUri(context, uri, "profilePhoto");
        } catch (IOException e) {
            result.setValue(false);
            return result;
        }

        authService.uploadProfilePhoto(id, part).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) result.postValue(true);
                else {
                    result.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.postValue(false);
            }
        });
        return result;
    }

    public LiveData<Result<LoginResponse>> login(LoginRequest dto) {
        MutableLiveData<Result<LoginResponse>> liveData = new MutableLiveData<>();
        authService.login(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    handleSuccessfulResponse(response.body(), liveData);
                else
                   handleErrorResponse(response, liveData);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    private void handleSuccessfulResponse(LoginResponse responseBody, MutableLiveData<Result<LoginResponse>> liveData) {
        saveJwtToken(responseBody.getJwt());
        liveData.postValue(Result.success(responseBody));
    }

    private void handleErrorResponse(Response<LoginResponse> response, MutableLiveData<Result<LoginResponse>> liveData) {
        try {
            String errorMessage = response.errorBody().string();
            liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorMessage)));
        } catch (IOException e) {
            liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
        }
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

    public String getUserRole() {
        return sharedPreferences.getString("role", null);
    }
    public String saveRole(String jwt) {
        String role = JwtDecoder.decodeRole(jwt);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("role", role);
        editor.apply();
        return role;
    }
}
