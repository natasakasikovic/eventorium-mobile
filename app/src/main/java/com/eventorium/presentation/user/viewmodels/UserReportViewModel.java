package com.eventorium.presentation.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.UpdateReportStatusRequest;
import com.eventorium.data.auth.models.UserReportRequest;
import com.eventorium.data.auth.models.UserReportResponse;
import com.eventorium.data.auth.repositories.UserReportRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserReportViewModel extends ViewModel {

    UserReportRepository repository;

    @Inject
    public UserReportViewModel(UserReportRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<UserReportResponse>>> getReports() {
        return repository.getReports();
    }

    public LiveData<Result<Void>> reportUser (Long id, UserReportRequest report) {
        return repository.reportUser(id, report);
    }

    public LiveData<Result<Void>> updateStatus(Long id, UpdateReportStatusRequest request){
        return repository.updateStatus(request, id);
    }
}
