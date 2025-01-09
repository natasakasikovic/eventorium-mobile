package com.eventorium.presentation.user.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.repositories.UserRepository;
import com.eventorium.data.util.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    UserRepository repository;

    @Inject
    public UserViewModel(UserRepository repository) {
        this.repository = repository;
    }

    public LiveData<Result<AccountDetails>> getCurrentUser() {
        return repository.getCurrentUser();
    }

    public LiveData<Bitmap> getProfilePhoto(Long id) {
        return repository.getProfilePhoto(id);
    }
}
