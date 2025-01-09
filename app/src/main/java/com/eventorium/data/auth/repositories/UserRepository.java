package com.eventorium.data.auth.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.services.UserService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRepository {
    private final UserService service;

    @Inject
    public UserRepository(UserService userService) {
        this.service = userService;
    }

    public LiveData<Result<AccountDetails>> getCurrentUser() {
        MutableLiveData<Result<AccountDetails>> liveData = new MutableLiveData<>();

        service.getCurrentUser().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<AccountDetails> call, Response<AccountDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Result.success(response.body()));
                } else {
                    try {
                        String errorResponse = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error("Error while loading"));
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountDetails> call, Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });

        return liveData;
    }

    public LiveData<Bitmap> getProfilePhoto(Long id) {
        MutableLiveData<Bitmap> liveData = new MutableLiveData<>();

        service.getProfilePhoto(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null) {
                    try (ResponseBody responseBody = response.body()) {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        liveData.postValue(bitmap);
                    } catch (Exception e) {
                        liveData.postValue(null);
                    }
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                liveData.postValue(null);
            }
        });

        return liveData;
    }

}
