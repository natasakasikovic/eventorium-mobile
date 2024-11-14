package com.eventorium.main.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;

public class SplashScreenViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    private static final int SPLASH_SCREEN_DURATION = 1500;

    public SplashScreenViewModel() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(SPLASH_SCREEN_DURATION);
                isLoading.postValue(false);
            } catch (InterruptedException ignored) {
            }
        });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
