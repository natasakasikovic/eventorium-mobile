package com.eventorium.data.auth.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.auth.models.UpdateReportStatusRequest;
import com.eventorium.data.auth.models.UserReportRequest;
import com.eventorium.data.auth.models.UserReportResponse;
import com.eventorium.data.auth.services.UserReportService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class UserReportRepository {

    private final UserReportService service;

    @Inject
    public UserReportRepository(UserReportService service) {
        this.service = service;
    }

    public LiveData<Result<List<UserReportResponse>>> getReports() {
        MutableLiveData<Result<List<UserReportResponse>>> liveData = new MutableLiveData<>();
        service.getReports().enqueue(handleGeneralResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> reportUser(Long id, UserReportRequest report) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        service.reportUser(report, id).enqueue(handleVoidResponse(liveData));
        return liveData;
    }

    public LiveData<Result<Void>> updateStatus(UpdateReportStatusRequest request, Long id) {
        MutableLiveData<Result<Void>> liveData = new MutableLiveData<>();
        service.updateReport(id, request).enqueue(handleVoidResponse(liveData));
        return liveData;
    }
}