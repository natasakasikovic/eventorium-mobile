package com.eventorium.data.notification.services;

import com.eventorium.data.notification.models.NotificationResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface NotificationService {

    @GET("notifications")
    Call<List<NotificationResponse>> getNotifications();

    @GET("notifications/silence")
    Call<Boolean> getNotificationSilenceStatus();
    @PATCH("notifications/silence")
    Call<ResponseBody> silenceNotifications(@Query("silence") boolean silence);
    @PATCH("notifications/seen")
    Call<ResponseBody> markNotificationsAsSeen();
}