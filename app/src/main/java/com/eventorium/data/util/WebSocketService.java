package com.eventorium.data.util;

import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

import io.reactivex.disposables.Disposable;
import okhttp3.WebSocketListener;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eventorium.BuildConfig;
import com.eventorium.data.auth.repositories.AuthRepository;

import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.*;
import okio.ByteString;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService {

    private static final String TAG = "WebSocketService";
    private static final String SERVER_URL = "ws://" + BuildConfig.IP_ADDR + ":8080/api/v1/ws/websocket";

    @SuppressLint("CheckResult")
    public void connect(Long userId) {
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SERVER_URL);
        stompClient.connect();
        logConnection(stompClient);

        stompClient.topic("/user/" + userId + "/notifications").subscribe(message -> {
            JSONObject jsonObject = new JSONObject(message.getPayload());
            Log.i(TAG, "Receive: " + jsonObject);
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
