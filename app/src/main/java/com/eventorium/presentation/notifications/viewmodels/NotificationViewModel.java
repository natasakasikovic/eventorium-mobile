package com.eventorium.presentation.notifications.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.notification.models.NotificationResponse;
import com.eventorium.data.notification.repositories.NotificationRepository;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NotificationViewModel extends ViewModel {

    private final NotificationRepository repository;

    @Inject
    public NotificationViewModel(NotificationRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<List<NotificationResponse>>> getNotifications() {
        return repository.getNotifications();
    }

    public LiveData<Result<Void>> markNotificationsAsSeen() {
        return repository.markNotificationsAsSeen();
    }
}