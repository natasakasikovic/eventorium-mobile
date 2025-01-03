package com.eventorium.data.event.repositories;

import androidx.annotation.NonNull;

import com.eventorium.data.event.models.Invitation;
import com.eventorium.data.event.services.InvitationService;
import com.eventorium.data.util.Result;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationRepository {

    private final InvitationService service;

    @Inject
    public InvitationRepository(InvitationService service){
        this.service = service;
    }

    public void sendInvitations(Long id, List<Invitation> invitations) {
        service.sendInvitations(id, invitations).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Invitations sent successfully!"); // TODO: remove this and notify user that everything is sent successfully
                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // TODO: handle error
            }
        });
    }

}