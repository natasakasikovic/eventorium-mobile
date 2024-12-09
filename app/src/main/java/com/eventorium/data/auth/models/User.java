package com.eventorium.data.auth.models;

public class User {
    private String email;
    private String password;
    private Role role;
    private String profilePicture;
    private boolean activated; // Whether it is activated via the activation link
    private boolean deactivated; // Whether the user has deactivated the account
}
