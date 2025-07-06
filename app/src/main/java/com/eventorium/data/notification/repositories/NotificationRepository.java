package com.eventorium.data.notification.repositories;

import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleGeneralResponse;
import static com.eventorium.data.shared.utils.RetrofitCallbackHelper.handleVoidResponse;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.notification.models.NotificationResponse;
import com.eventorium.data.notification.services.NotificationService;
import com.eventorium.data.shared.models.Result;

import java.util.List;

import javax.inject.Inject;

public class NotificationRepository {

    private final NotificationService service;
    private final SharedPreferences sharedPreferences;

    @Inject
    public NotificationRepository(SharedPreferences sharedPreferences, NotificationService service) {
        this.sharedPreferences = sharedPreferences;
        this.service = service;
    }

    public LiveData<Result<List<NotificationResponse>>> getNotifications() {
        MutableLiveData<Result<List<NotificationResponse>>> result = new MutableLiveData<>();
        service.getNotifications().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> markNotificationsAsSeen() {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.markNotificationsAsSeen().enqueue(handleVoidResponse(result));
        return result;
    }

    public LiveData<Result<Boolean>> getNotificationSilenceStatus() {
        MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        service.getNotificationSilenceStatus().enqueue(handleGeneralResponse(result));
        return result;
    }

    public LiveData<Result<Void>> silenceNotifications(boolean silence) {
        MutableLiveData<Result<Void>> result = new MutableLiveData<>();
        service.silenceNotifications(silence).enqueue(handleVoidResponse(result));
        return result;
    }

    public void saveSilencedStatus(boolean silenced) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("silenceNotifications", silenced);
        editor.apply();
    }
}