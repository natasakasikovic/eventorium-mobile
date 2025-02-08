package com.eventorium.data.notifications.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.notifications.models.NotificationResponse;
import com.eventorium.data.notifications.services.NotificationService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {

    private final NotificationService service;

    @Inject
    public NotificationRepository(NotificationService service) {
        this.service = service;
    }

    public LiveData<Result<List<NotificationResponse>>> getNotifications() {
        MutableLiveData<Result<List<NotificationResponse>>> result = new MutableLiveData<>();

        service.getNotifications().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationResponse>> call, @NonNull Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success(response.body()));
                else {
                    try {
                        String errResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationResponse>> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });

        return result;
    }

    public LiveData<Result<Void>> markNotificationsAsSeen() {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();

        service.markNotificationsAsSeen().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    result.postValue(Result.success(null));
                else {
                    try {
                        String errResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        });
        return result;
    }
}