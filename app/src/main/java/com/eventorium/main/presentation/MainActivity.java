package com.eventorium.main.presentation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);
    }
}