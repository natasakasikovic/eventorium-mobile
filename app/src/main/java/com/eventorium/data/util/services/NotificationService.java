package com.eventorium.data.util.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.media.RingtoneManager;


import androidx.core.app.NotificationCompat;


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

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(soundUri)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            Log.e("Notification", "NotificationManager is null");
        }
    }

}
