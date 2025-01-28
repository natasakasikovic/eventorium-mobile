package com.eventorium.data.auth.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.UpdateReportStatusRequest;
import com.eventorium.data.auth.models.UserReportRequest;
import com.eventorium.data.auth.models.UserReportResponse;
import com.eventorium.data.auth.services.UserReportService;
import com.eventorium.data.util.ErrorResponse;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.constants.ErrorMessages;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReportRepository {

    private final UserReportService service;

    @Inject
    public UserReportRepository(UserReportService service) {
        this.service = service;
    }

    public LiveData<Result<List<UserReportResponse>>> getReports() {
        MutableLiveData<Result<List<UserReportResponse>>> liveData = new MutableLiveData<>();

        service.getReports().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<UserReportResponse>> call, @NonNull Response<List<UserReportResponse>> response) {
                if (response.body() != null && response.isSuccessful())
                    liveData.postValue(Result.success(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<UserReportResponse>> call, @NonNull Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    public LiveData<Result<Void>> reportUser(Long id, UserReportRequest report) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        service.reportUser(report, id).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    liveData.postValue(Result.success(null));
                else {
                    try {
                        String error = response.errorBody().string();
                        liveData.postValue(Result.error(ErrorResponse.getErrorMessage(error)));
                    } catch (IOException e) {
                        liveData.postValue(Result.error(ErrorMessages.VALIDATION_ERROR));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }

    public LiveData<Result<Void>> updateStatus(UpdateReportStatusRequest request, Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();

        service.updateReport(id, request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful())
                    liveData.postValue(Result.success(null));
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                liveData.postValue(Result.error(t.getMessage()));
            }
        });
        return liveData;
    }
}