package com.eventorium.data.util.services;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.eventorium.BuildConfig;
import com.eventorium.Eventorium;
import com.eventorium.data.interaction.models.ChatMessage;
import com.eventorium.data.interaction.models.ChatMessageRequest;
import com.eventorium.data.interaction.models.Notification;
import com.eventorium.data.util.adapters.LocalDateAdapter;
import com.eventorium.data.util.adapters.LocalDateTimeAdapter;
import com.eventorium.presentation.chat.fragments.ChatFragment;
import com.eventorium.presentation.util.listeners.OnMessageReceive;
import com.eventorium.presentation.util.services.NotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Setter;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebSocketService {

    private static final String TAG = "WebSocketService";
    private static final String SERVER_URL = "ws://" + BuildConfig.IP_ADDR + ":8080/api/v1/ws/websocket";
    private StompClient stompClient;
    private final NotificationService notificationService;
    @Setter
    private OnMessageReceive chatMessageListener;
    private Long userId;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

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
        stompClient.topic("/user/" + userId + "/notifications")
                .subscribe(this::handleNotification);
        stompClient.topic("/user/" + userId + "/queue/messages")
                .subscribe(this::handleChatMessage);
    }


    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    private void createAdminSubscriptions() {
        stompClient.topic("/topic/admin").subscribe(this::handleNotification);
    }


    private void handleNotification(StompMessage message) {
        Notification notification = gson.fromJson(message.getPayload(), Notification.class);
        new Handler(Looper.getMainLooper()).post(() -> {
            notificationService.showNotification(notification.getTitle(), notification.getMessage());
        });
    }

    private void handleChatMessage(StompMessage message) {
        ChatMessage chatMessage = gson.fromJson(message.getPayload(), ChatMessage.class);
        if (chatMessageListener != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                chatMessageListener.onNewChatMessage(chatMessage);
            });
        } else {
            notificationService.showChatNotification("New Message", chatMessage.getMessage(), chatMessage.getSenderId());
        }
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

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CheckResult"})
    public void sendMessage(ChatMessageRequest message) {
        stompClient.send("/app/chat", gson.toJson(message))
                .subscribe(() -> Log.d(TAG, "Message sent successfully"),
                        throwable -> Log.e(TAG, "Failed to send message", throwable));
    }

    public void disconnect() {
        if(stompClient != null) {
            stompClient.disconnect();
        }
    }

}
