package com.eventorium.presentation.auth.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.repositories.AuthRepository;
import com.eventorium.data.shared.models.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository repository;

    @Inject
    public AuthViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<User>> createAccount(User user) {
        return repository.createAccount(user);
    }

    public LiveData<Boolean> uploadProfilePhoto(Long id, Context context, Uri uri) {
        return repository.uploadPhoto(id, context, uri);
    }

    public String getUserRole() {
        return repository.getUserRole();
    }

    public Long getUserId() {
        return repository.getUserId();
    }
}
