package com.eventorium.data.notification.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.*;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.notification.models.NotificationResponse;
import com.eventorium.data.notification.services.NotificationService;
import com.eventorium.data.shared.models.ErrorResponse;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.constants.ErrorMessages;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

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
        service.getNotifications().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> markNotificationsAsSeen() {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.markNotificationsAsSeen().enqueue(handleGeneralResponse(result));
        return result;
    }
}