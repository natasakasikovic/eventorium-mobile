package com.eventorium.presentation.auth.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.LoginRequest;
import com.eventorium.data.auth.models.AuthResponse;
import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.services.WebSocketService;

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

    public LiveData<Result<AuthResponse>> login(LoginRequest dto) {
        return authRepository.login(dto);
    }

    public String saveRole(String jwt) {
        return authRepository.saveRole(jwt);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
    public String getUserRole() {
        return authRepository.getUserRole();
    }

    public Long getUserId() { return authRepository.getUserId(); }
}
