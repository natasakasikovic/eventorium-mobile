package com.eventorium.data.auth.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.services.UserService;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserService service;

    @Inject
    public UserRepository(UserService service) {
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
}
