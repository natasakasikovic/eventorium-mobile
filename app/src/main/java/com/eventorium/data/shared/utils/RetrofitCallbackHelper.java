package com.eventorium.data.shared.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.Role;
import com.eventorium.data.event.models.Event;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitCallbackHelper {

    public static Callback<ResponseBody> handleDelete(MutableLiveData<Result<Void>> liveData) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (response.isSuccessful()) {
                    liveData.postValue(Result.success(null));
                } else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                liveData.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

    public static<T> Callback<T> handleResponse(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if(response.isSuccessful() && response.body() != null) {
                    result.postValue(Result.success(response.body()));
                } else {
                    result.postValue(Result.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

    public static<T> Callback<T> handleValidationResponse(MutableLiveData<Result<T>> result) {
        return new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<T> call,
                    @NonNull Response<T> response
            ) {
                if (response.isSuccessful() && response.body() != null)
                    result.postValue(Result.success((response.body())));
                else {
                    try {
                        String errorResponse = response.errorBody().string();
                        result.postValue(Result.error(ErrorResponse.getErrorMessage(errorResponse)));
                    } catch (IOException e) {
                        result.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                result.postValue(Result.error(ErrorMessages.GENERAL_ERROR));
            }
        };
    }

    public static<T> Callback<T> handleSuccessfulResponse(MutableLiveData<T> data) {
        return new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.body() != null && response.isSuccessful()) {
                    data.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                data.postValue(null);
            }
        };
    }
}
