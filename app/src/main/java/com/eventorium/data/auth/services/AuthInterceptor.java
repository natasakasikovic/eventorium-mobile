package com.eventorium.data.auth.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.eventorium.presentation.MainActivity;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AuthInterceptor implements Interceptor {

    private final SharedPreferences sharedPreferences;
    private final Context context;

    @Inject
    public AuthInterceptor(SharedPreferences sharedPreferences, @ApplicationContext Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
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
        Response response = chain.proceed(newRequest);

        if (response.code() == 401) {
            ResponseBody responseBody = response.body();
            String responseBodyString = responseBody != null ? responseBody.string() : "";

            if (responseBodyString.contains("Token expired")) {
                sharedPreferences.edit().clear().apply();

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                response.close();
            }

            return response.newBuilder()
                    .body(ResponseBody.create(responseBodyString, responseBody.contentType()))
                    .build();
        }

        return response;
    }

}
