package com.eventorium.data.auth.repositories;

import com.eventorium.data.auth.models.User;
import com.eventorium.data.auth.services.AuthService;
import com.eventorium.data.auth.services.UserService;

import javax.inject.Inject;


public class UserRepository {
    private final UserService service;

    @Inject
    public UserRepository(UserService userService) {
        this.service = userService;
    }

}
