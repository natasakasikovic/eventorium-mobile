package com.eventorium.presentation.util.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.media.RingtoneManager;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.eventorium.presentation.MainActivity;
import com.eventorium.presentation.chat.fragments.ChatFragment;


public class NotificationService {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_id";

    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void createNotificationChannel() {
        CharSequence name = "Default Channel";
        String description = "Channel for notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String contentText) {
        createNotificationChannel();
        sendNotification(buildNotification(title, contentText, null).build());
    }

    public void showChatNotification(
            String title,
            String contentText,
            Long recipientId
    ) {
        createNotificationChannel();
        sendNotification(buildNotification(title, contentText, configureChatIntent(recipientId))
                .build());
    }

    private PendingIntent configureChatIntent(Long recipientId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("openFragment", "ChatFragment");
        intent.putExtra(ChatFragment.ARG_RECIPIENT_ID, recipientId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private NotificationCompat.Builder buildNotification(
            String title,
            String contentText,
            @Nullable PendingIntent pendingIntent
    ) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private void sendNotification(Notification notification) {
        NotificationManager notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            Log.e("Notification", "NotificationManager is null");
        }
    }

}
