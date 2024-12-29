package com.eventorium.data.util.services;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.eventorium.BuildConfig;
import com.eventorium.Eventorium;
import com.eventorium.data.util.models.Notification;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebSocketService {

    private static final String TAG = "WebSocketService";
    private static final String SERVER_URL = "ws://" + BuildConfig.IP_ADDR + ":8080/api/v1/ws/websocket";
    private StompClient stompClient;
    private final NotificationService notificationService;
    private Long userId;

    public WebSocketService() {
        this.notificationService = new NotificationService(Eventorium.getAppContext());
    }

    @SuppressLint("CheckResult")
    public void connect(Long userId, String role) {
        if(userId == null) {
            return;
        }
        this.userId = userId;
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL);
        stompClient.connect();
        logConnection(stompClient);

        createGlobalSubscriptions();
        switch(role) {
            case "ADMIN" -> createAdminSubscriptions();
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    private void createGlobalSubscriptions() {
        stompClient.topic("/user/" + userId + "/notifications").subscribe(this::handleNotification);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    private void createAdminSubscriptions() {
        stompClient.topic("/topic/admin").subscribe(this::handleNotification);
    }


    private void handleNotification(StompMessage message) {
        Gson gson = new Gson();
        Notification notification = gson.fromJson(message.getPayload(), Notification.class);
        new Handler(Looper.getMainLooper()).post(() -> {
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

    public void disconnect() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
    }

}
