package com.eventorium.data.auth.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.util.FileUtil;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final AuthService service;

    @Inject
    public UserRepository(AuthService service) {
        this.service = service;
    }

    public LiveData<Result<User>> createAccount(User user) {
        MutableLiveData<Result<User>> liveData = new MutableLiveData<>();
        service.createAccount(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    try {
                        // TODO: convert to json and extract error message
                        Log.i("OVO JE GRESKA", response.errorBody().string());
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(errorResponse));
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
}
