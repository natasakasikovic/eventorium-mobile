package com.eventorium.presentation.auth.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.eventorium.data.auth.models.Role;
import com.eventorium.data.auth.repositories.RoleRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RoleViewModel extends ViewModel {
    private final RoleRepository repository;

    @Inject
    public RoleViewModel(RoleRepository roleRepository) {
        this.repository = roleRepository;
    }

    public LiveData<List<Role>> getRegistrationRoles() {
        return repository.getRegistrationRoles();
    }
}
