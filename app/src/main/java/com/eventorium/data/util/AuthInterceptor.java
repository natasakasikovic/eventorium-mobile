package com.eventorium.data.util;

import android.content.SharedPreferences;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SharedPreferences sharedPreferences;

    @Inject
    public AuthInterceptor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = sharedPreferences.getString("user", null);

        Request originalRequest = chain.request();
        if (originalRequest.header("skip") != null) {
            return chain.proceed(originalRequest);
        }

        Request.Builder requestBuilder = originalRequest.newBuilder();
        if (accessToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
        }

        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}
