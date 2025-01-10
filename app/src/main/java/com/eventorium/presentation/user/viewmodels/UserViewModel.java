package com.eventorium.presentation.user.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.AccountDetails;
import com.eventorium.data.auth.models.ChangePasswordRequest;
import com.eventorium.data.auth.models.Person;
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

    public LiveData<Result<Void>> update(Person updateRequest) {
        return repository.update(updateRequest);
    }

    public LiveData<Boolean> updateProfilePhoto(Context context, Uri uri) {
        return repository.uploadPhoto(context, uri);
    }

    public LiveData<Result<Void>> changePassword(ChangePasswordRequest request) {
        return repository.changePassword(request);
    }
}
