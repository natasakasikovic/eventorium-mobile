package com.eventorium.data.auth.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.JWT;
import com.eventorium.data.auth.models.LoginRequest;
import com.eventorium.data.auth.models.AuthResponse;
import com.eventorium.data.auth.models.UpgradeAccountRequest;
import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.utils.FileUtil;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.utils.JwtDecoder;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final AuthService service;
    private final SharedPreferences sharedPreferences;

    @Inject
    public AuthRepository(AuthService authService, SharedPreferences sharedPreferences) {
        this.service = authService;
        this.sharedPreferences = sharedPreferences;
    }

    public LiveData<Result<User>> createAccount(User user) {
        MutableLiveData<Result<User>> liveData = new MutableLiveData<>();
        service.createAccount(user).enqueue(RetrofitCallbackHelper.handleValidationResponse(liveData));
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

        service.uploadProfilePhoto(id, part).enqueue(new Callback<>() {
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

    public LiveData<Result<AuthResponse>> login(LoginRequest dto) {
        MutableLiveData<Result<AuthResponse>> liveData = new MutableLiveData<>();
        service.login(dto).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    handleSuccessfulResponse(response.body(), liveData);
                else
                   handleErrorResponse(response, liveData);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    private void handleSuccessfulResponse(AuthResponse responseBody, MutableLiveData<Result<AuthResponse>> liveData) {
        saveJwtToken(responseBody.getJwt());
        liveData.postValue(Result.success(responseBody));
    }

    private void handleErrorResponse(Response<AuthResponse> response, MutableLiveData<Result<AuthResponse>> liveData) {
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

    public LiveData<Result<AuthResponse>> upgradeAccount(UpgradeAccountRequest request) {
        MutableLiveData<Result<AuthResponse>> result = new MutableLiveData<>();
        service.upgradeAccount(request).enqueue(handleGeneralResponse(result));
        return result;
    }

    public void updateSession(AuthResponse response) {
        saveJwtToken(response.getJwt());
        saveRole(response.getJwt());
    }
}
