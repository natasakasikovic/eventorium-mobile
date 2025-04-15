package com.eventorium.data.notification.services;

import com.eventorium.data.notification.models.NotificationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;

public interface NotificationService {

    @GET("/api/v1/notifications")
    Call<List<NotificationResponse>> getNotifications();

    @PATCH("/api/v1/notifications/seen")
    Call<Void> markNotificationsAsSeen();
}