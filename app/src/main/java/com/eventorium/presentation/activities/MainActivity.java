package com.eventorium.presentation.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.eventorium.R;
import com.eventorium.presentation.viewmodels.SplashScreenViewModel;

public class MainActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);
        SplashScreen
                .installSplashScreen(this)
                .setKeepOnScreenCondition(() -> Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}