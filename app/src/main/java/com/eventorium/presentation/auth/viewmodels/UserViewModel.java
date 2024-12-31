package com.eventorium.presentation.auth.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.repositories.UserRepository;
import com.eventorium.data.util.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    UserRepository repository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public LiveData<Result<User>> createAccount(User user) {
        return repository.createAccount(user);
    }

    public LiveData<Boolean> uploadProfilePhoto(Long id, Context context, Uri uri) {
        return repository.uploadPhoto(id, context, uri);
    }
}
