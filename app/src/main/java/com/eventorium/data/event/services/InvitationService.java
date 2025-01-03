package com.eventorium.data.event.services;

import com.eventorium.data.event.models.Invitation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvitationService {

    @POST("invitations/{event-id}")
    Call<Void> sendInvitations(@Path("event-id") Long id , @Body List<Invitation> invitations);

}