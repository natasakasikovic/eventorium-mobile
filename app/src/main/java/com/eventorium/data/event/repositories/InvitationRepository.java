package com.eventorium.data.event.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.eventorium.data.event.models.invitation.Invitation;
import com.eventorium.data.event.models.invitation.InvitationDetails;
import com.eventorium.data.event.services.InvitationService;
import com.eventorium.data.shared.models.Result;
import com.eventorium.data.shared.utils.RetrofitCallbackHelper;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
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
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Invitations sent successfully!"); // TODO: remove this and notify user that everything is sent successfully
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                // TODO: handle error
            }
        });
    }

    public LiveData<Result<List<InvitationDetails>>> getInvitations() {
        MutableLiveData<Result<List<InvitationDetails>>> result = new MutableLiveData<>();
        service.getInvitations().enqueue(RetrofitCallbackHelper.handleGeneralResponse(result));
        return result;
    }

}