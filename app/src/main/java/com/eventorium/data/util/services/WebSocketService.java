package com.eventorium.data.util.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.eventorium.BuildConfig;
import com.eventorium.Eventorium;
import com.eventorium.data.util.models.Notification;
import com.google.gson.Gson;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService {

    private static final String TAG = "WebSocketService";
    private static final String SERVER_URL = "ws://" + BuildConfig.IP_ADDR + ":8080/api/v1/ws/websocket";
    private final NotificationService notificationService;

    public WebSocketService() {
        this.notificationService = new NotificationService(Eventorium.getAppContext());
    }

    @SuppressLint("CheckResult")
    public void connect(Long userId) {
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL);
        stompClient.connect();
        logConnection(stompClient);

        Gson gson = new Gson();

        stompClient.topic("/user/" + userId + "/notifications").subscribe(message -> {
            Notification notification = gson.fromJson(message.getPayload(), Notification.class);
            notificationService.showNotification("Notification", notification.getMessage());
        });
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    private void logConnection(StompClient stompClient) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });
    }

}
