package com.eventorium.data.event.services;

import com.eventorium.data.event.models.Invitation;
import com.eventorium.data.event.models.InvitationDetails;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvitationService {

    @POST("invitations/{event-id}")
    Call<ResponseBody> sendInvitations(@Path("event-id") Long id , @Body List<Invitation> invitations);

    @GET("invitations/my-invitations")
    Call<List<InvitationDetails>> getInvitations();
}