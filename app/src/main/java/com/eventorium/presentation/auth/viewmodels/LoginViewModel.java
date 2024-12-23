package com.eventorium.presentation.auth.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.dtos.LoginRequestDto;
import com.eventorium.data.auth.dtos.LoginResponseDto;
import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.util.Result;
import com.eventorium.data.util.services.WebSocketService;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final WebSocketService webSocketService;

    @Inject
    public LoginViewModel(WebSocketService webSocketService, AuthRepository authRepository) {
        this.webSocketService = webSocketService;
        this.authRepository = authRepository;
    }

    public LiveData<Result<LoginResponseDto>> login(LoginRequestDto dto) {
        return authRepository.login(dto);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void openWebSocket() {
        webSocketService.connect(authRepository.getUserId());
    }
}
