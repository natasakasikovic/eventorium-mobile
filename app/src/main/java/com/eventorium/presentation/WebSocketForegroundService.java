package com.eventorium.presentation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.eventorium.R;
import com.eventorium.data.shared.services.WebSocketService;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WebSocketForegroundService extends Service {

    public static boolean isRunning = false;
    private static final String TAG = "WebSocketFgService";
    private static final String CHANNEL_ID = "WebSocketChannel";
    private static final int NOTIFICATION_ID = 101;
    @Inject
    WebSocketService webSocketService;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null) return START_STICKY;
        long userId = intent.getLongExtra("userId", -1);
        String role = intent.getStringExtra("role");

        if (userId != -1 && role != null) {
            Log.i(TAG, "Opening WebSocket");
            webSocketService.connect(userId, role);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Eventorium")
                .setContentText("Listening for notifications and messages...")
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "WebSocket Background Channel",
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription("Keeps WebSocket connection alive in background");

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketService.disconnect();
        Log.d(TAG, "WebSocket service destroyed and disconnected");
        isRunning = false;
    }
}
