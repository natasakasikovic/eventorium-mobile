package com.eventorium.presentation.shared.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executors;

import lombok.Getter;

@Getter
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

}
